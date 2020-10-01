package axela.insurance;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insurance_Followup_Status extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "0";
	public String comp_id = "0";
	public String[] team_ids, exe_ids, model_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", enquiry_Model = "", item_Model = "";
	DecimalFormat deci = new DecimalFormat("#");
	public String go = "";
	public String ExeAccess = "";
	public MIS_Check misCheck = new MIS_Check();
	public String insur = "", StrFilter = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				insur = PadQuotes(request.getParameter("insur"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					// StrSearch = BranchAccess.replaceAll("branch_id", "emp_branch_id").replace(")", "  or emp_branch_id=0)") + " " + ExeAccess;
					// SOP("StrSearch = " + StrSearch);
					StrSearch = ExeAccess;
					if (!exe_id.equals("")) {
						StrSearch += " AND emp_id in (" + exe_id + ")";
					}
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				} else {
					StrHTML = ListTarget();
				}
				// SOP("insur=1=" + insur);
				if (!insur.equals("")) {
					InsuranceEnquiry(request, response);
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
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
	}

	protected void CheckForm() {
		msg = "";

		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		}

		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>Select End Date!<br>";
		}

		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListTarget() {
		int empid = 0, empcount = 0;
		// int total_lead = 0;
		int total_insurenquiry = 0;
		int total_converted = 0;
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT "
				+ " emp_id, "
				+ " emp_name, "
				+ " emp_ref_no, "
				+ " (SELECT	COALESCE (COUNT(DISTINCT insurenquiry_id),0) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
				+ " WHERE 1 = 1 "
				+ " AND SUBSTR(insurenquiry_entry_date,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(insurenquiry_entry_date,1,8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " AND insurenquiry_emp_id = emp_id ) AS insurenquirycount, "
				+ " (SELECT COALESCE (count(DISTINCT insurpolicy_id),0) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_policy "
				+ " WHERE 1 = 1 "
				+ " AND	substr(insurpolicy_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND substr(insurpolicy_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " AND insurpolicy_active = 1 "
				+ " AND insurpolicy_emp_id = emp_id	) AS converted "
				+ " FROM " + compdb(comp_id) + "axela_emp "
				+ " WHERE 1 = 1 "
				+ " AND emp_active = '1' "
				+ " AND emp_insur = '1' "
				+ StrSearch + ""
				+ " GROUP BY emp_name,emp_id "
				+ " ORDER BY emp_name ";

		// SOP("StrSql89===" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"  table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Insurance Enquiry</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Converted</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				int rowcount = crs.getRow();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					if (empid != crs.getInt("emp_id")) {
						empcount++;
						empid = crs.getInt("emp_id");
					}
					// total_lead = total_lead + crs.getInt("leadcount");
					total_insurenquiry = total_insurenquiry + crs.getInt("insurenquirycount");
					total_converted = total_converted + crs.getInt("converted");

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + empcount + "</td>");
					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id") + ">" + crs.getString("emp_name") + " ("
							+ crs.getString("emp_ref_no") + ")</a></td>");

					// / Opportunity
					Str.append("<td valign=top align=right><a href=../insurance/report-insurance-followup-status.jsp?insur=count&starttime="
							+ starttime + "&endtime=" + endtime + ""
							+ "&insuremp_id=" + crs.getString("emp_id") + " target=_blank>"
							+ crs.getInt("insurenquirycount") + "</a></td>");
					Str.append("<td valign=top align=right><a href=../insurance/report-insurance-followup-status.jsp?insur=converted&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&insuremp_id=" + crs.getString("emp_id") + " target=_blank>" + crs.getInt("converted") + "</a></td>");
					Str.append("</tr>\n");
				}
				if (empcount > 1) {
					Str.append("<tr>\n");
					Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
					Str.append("<td valign=top align=right><b>Total:</b></td>");

					// / Opportunity
					Str.append("<td valign=top align=right><a href=../insurance/report-insurance-followup-status.jsp?insur=count&starttime=" + starttime + "&endtime=" + endtime + ""
							+ " target=_blank><b>" + total_insurenquiry + "</b></a></td>");

					// / Meeting Planned
					Str.append("<td valign=top align=right><a href=../insurance/report-insurance-followup-status.jsp?insur=converted&starttime=" + starttime + "&endtime=" + endtime + ""
							+ " target=_blank>" + total_converted + "</a></td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	private void InsuranceEnquiry(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			// String insur = PadQuotes(request.getParameter("insur"));
			String insuremp_id = PadQuotes(request.getParameter("insuremp_id"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			// SOP("insur==" + insur);
			if (insur.equals("count")) {
				if (!insuremp_id.equals("")) {
					StrFilter = " AND insurenquiry_emp_id =" + insuremp_id;
				}
				StrFilter += " AND substr(insurenquiry_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND substr(insurenquiry_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
						+ " AND emp_insur = 1";

				SetSession("insurstrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("insurance-enquiry-list.jsp?smart=yes"));
			} else {
				if (!insuremp_id.equals("")) {
					StrFilter = " AND insurpolicy_emp_id =" + insuremp_id;
				}
				StrFilter += " AND substr(insurpolicy_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND substr(insurpolicy_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";

				SetSession("insurancestrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-list.jsp?smart=yes"));
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
