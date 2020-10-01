package axela.service;
//divya 30th may 2013
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_PsfFollowup_Analysis extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", comp_id = "0", jc_branch_id = "";
	public String[] exe_ids, brand_ids, region_ids, jc_branch_ids, crm_emp_ids, jc_emp_ids, psfdays_ids, jctype_ids, model_ids;
	public String team_id = "", crm_emp_id = "", jc_emp_id = "", psfdays_id = "", jctype_id = "", model_id = "";
	public String StrHTML = "", StrHTML1 = "", strHTML2 = "", StrClosedHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String brand_id = "", region_id = "";
	public String StrSearch = "", PsfStrSearch = "", CrmStrSearch = "";
	public String BookingSearchStr = "", FollowupSearchStr = "";
	public String EmpSearch = "";
	public String ExeAccess = "";
	public String go = "";
	public String bookingvehcount = "";
	public int TotalRecords = 0;
	public String StrSql = "", StrmodelSearch = "";
	public String TeamJoin = "";
	public String emp_all_exe = "";
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	// public String crmfollowupdays_id = "";
	DecimalFormat deci = new DecimalFormat("###.##");
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				// BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				bookingvehcount = PadQuotes(request.getParameter("bookingvehcount"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {

					StrSearch += BranchAccess.replace("branch_id", "jc_branch_id");

					if (!brand_id.equals("") && jc_branch_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!jc_branch_id.equals("")) {
						StrSearch += " AND jc_branch_id IN (" + jc_branch_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND variant_preownedmodel_id IN (" + model_id + ")";
					}
					if (!starttime.equals("")) {
						StrSearch += " AND SUBSTR(jc_time_out, 1, 8) >= SUBSTR('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(jc_time_out, 1, 8) <= SUBSTR('" + endtime + "',1,8)";
					}
					if (!jc_emp_id.equals("")) {
						PsfStrSearch = " AND jc_emp_id IN (" + jc_emp_id + ")";
					}
					if (!psfdays_id.equals("")) {
						StrSearch += " AND jcpsf_psfdays_id IN (" + psfdays_id + ")";
					}
					if (!jctype_id.equals("")) {
						StrSearch += " AND jc_jctype_id IN (" + jctype_id + ")";
					}
					if (!crm_emp_id.equals("")) {
						CrmStrSearch = " AND jcpsf_emp_id IN (" + crm_emp_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = AdviserFollowupDetails();
						StrHTML1 = CRMFollowupDetails();
						strHTML2 = TechnicianFollowupDetails();
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
		// SOP("starttime---------------" + starttime);
		// SOP("endtime--------" + endtime);

		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		jc_branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		jc_branch_ids = request.getParameterValues("dr_branch_id");

		jc_emp_id = RetrunSelectArrVal(request, "dr_jc_emp_id");
		jc_emp_ids = request.getParameterValues("dr_jc_emp_id");

		crm_emp_id = RetrunSelectArrVal(request, "dr_emp_id");
		crm_emp_ids = request.getParameterValues("dr_emp_id");
		psfdays_id = RetrunSelectArrVal(request, "dr_crmdays_id");
		psfdays_ids = request.getParameterValues("dr_crmdays_id");
		jctype_id = RetrunSelectArrVal(request, "dr_jc_jctype_id");
		jctype_ids = request.getParameterValues("dr_jc_jctype_id");
		model_id = RetrunSelectArrVal(request, "dr_model_id");
		model_ids = request.getParameterValues("dr_model_id");
	}
	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String AdviserFollowupDetails() {
		try {
			int count = 0;
			double followupcount = 0;
			int grandfollowupcount = 0;

			double contactable = 0;
			int grandcontactable = 0;
			double contactable_perc = 0;
			double grandcontactable_perc = 0;

			double noncontactable = 0;
			int grandnoncontactable = 0;
			double noncontactable_perc = 0;
			double grandnoncontactable_perc = 0;

			double satisfied = 0;
			double grandsatisfied = 0;

			double grandsatisfiedperc = 0;
			double dissatisfied = 0;
			double granddissatisfied = 0;
			double granddissatisfiedperc = 0;
			double satisfied_perc = 0.00;
			double dissatisfied_perc = 0.00;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT emp_id, emp_name, "
					// + " COUNT(DISTINCT veh_id) AS followupcount, "
					+ " COUNT(DISTINCT jcpsf_id) AS followupcount, "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id = 1 THEN jcpsf_id END ) AS contactable, "
					+ "	COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id >= 2 THEN jcpsf_id END ) AS noncontactable, "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 1 THEN jcpsf_id END ) AS satisfied, "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 2 THEN jcpsf_id END ) AS dissatisfied"
					// + " SUM(IF(jcpsf_psffeedbacktype_id = 1,1,0)) as contactable, "
					// + " SUM(IF(jcpsf_psffeedbacktype_id >= 2,1,0)) as noncontactable, "
					// + " SUM(IF(jcpsf_satisfied = 1,1,0)) as satisfied, "
					// + " SUM(IF(jcpsf_satisfied = 2,1,0)) as dissatisfied "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp jc ON emp_id = jc_emp_id "
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1"
					+ StrSearch
					+ PsfStrSearch
					+ CrmStrSearch;

			if (emp_all_exe.equals("0"))
			{
				StrSql += ExeAccess;
			}
			StrSql += " GROUP BY jc_emp_id"
					+ " ORDER BY emp_name";
			// + " LIMIT 1000";

			// SOP("StrSql====Adviser=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" style=\"text-align:center\">#</th>\n");
				Str.append("<th data-toggle=\"true\" align=center>Service Advisor</th>\n");
				Str.append("<th>Total</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Contact</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Contact %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" align=center>Not-Contacted</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" align=center>Not-Contacted %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center>Satisfied </th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Satisfied %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied%</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					satisfied = 0.00;
					contactable = 0.00;
					contactable_perc = 0.00;
					noncontactable_perc = 0.00;
					satisfied_perc = 0.00;
					dissatisfied_perc = 0.00;

					followupcount = crs.getDouble("followupcount");
					contactable = crs.getDouble("contactable");
					noncontactable = crs.getDouble("noncontactable");
					satisfied = crs.getDouble("satisfied");
					dissatisfied = crs.getDouble("dissatisfied");

					if (contactable != 0) {
						satisfied_perc = (satisfied / contactable) * 100;
						contactable_perc = (contactable / followupcount) * 100;
					}
					if (contactable != 0) {
						dissatisfied_perc = (dissatisfied / contactable) * 100;
					}

					if (noncontactable != 0) {
						noncontactable_perc = (noncontactable / followupcount) * 100;
					}

					grandfollowupcount += followupcount;
					grandcontactable += contactable;
					grandnoncontactable += noncontactable;
					grandsatisfied += satisfied;
					granddissatisfied += dissatisfied;
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"))
							.append("</a></td>\n");
					Str.append("<td align=right>").append(crs.getString("followupcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("contactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(contactable_perc)).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("noncontactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(noncontactable_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("satisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(satisfied_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("dissatisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(dissatisfied_perc)).append("</td>\n");

					Str.append("</tr>");

				}
				if (grandcontactable != 0)
				{
					grandsatisfiedperc = (grandsatisfied / grandcontactable * 100);
					grandcontactable_perc = ((grandcontactable + 0.0) / grandfollowupcount) * 100;
				}
				if (grandcontactable != 0)
				{
					granddissatisfiedperc = (granddissatisfied / grandcontactable * 100);
				}
				if (grandnoncontactable != 0)
				{
					grandnoncontactable_perc = ((grandnoncontactable + 0.0) / grandfollowupcount) * 100;
				}
				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(grandfollowupcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandcontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandcontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandnoncontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandnoncontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandsatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandsatisfiedperc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(granddissatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(granddissatisfiedperc)).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
			else {
				Str.append("<font color=red><b>No Service Advisor found!</b></font>");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String CRMFollowupDetails() {
		try {
			int count = 0;
			double followupcount = 0;
			int grandfollowupcount = 0;

			double contactable = 0;
			int grandcontactable = 0;
			double contactable_perc = 0;
			double grandcontactable_perc = 0;

			double noncontactable = 0;
			int grandnoncontactable = 0;
			double noncontactable_perc = 0;
			double grandnoncontactable_perc = 0;

			double satisfied = 0;
			double grandsatisfied = 0;
			double grandsatisfiedperc = 0.00;
			double dissatisfied = 0;
			double granddissatisfied = 0;
			double granddissatisfiedperc = 0.00;
			double satisfied_perc = 0.00;
			double dissatisfied_perc = 0.00;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT emp_id, emp_name,"
					+ " count(DISTINCT jcpsf_id) AS followupcount, "
					+ " SUM(IF(jcpsf_psffeedbacktype_id = 1,1,0)) as contactable, "
					+ " SUM(IF(jcpsf_psffeedbacktype_id >= 2,1,0)) as noncontactable, "
					+ " SUM(IF(jcpsf_satisfied = 1,1,0)) as satisfied, "
					+ " SUM(IF(jcpsf_satisfied = 2,1,0)) as dissatisfied "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jcpsf_emp_id "
					+ " WHERE 1 = 1 "
					+ " AND emp_active = 1"
					+ StrSearch
					+ PsfStrSearch
					+ CrmStrSearch;

			if (emp_all_exe.equals("0"))
			{
				StrSql += ExeAccess;
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// + " LIMIT 1000";

			// SOP("StrSql====CRMFollowup============" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th style=\"text-align:center\" data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\" align=center>CRM Name</th>\n");
				Str.append("<th align=center>Total</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Contact</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Contact %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" align=center>Not-Contacted</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" align=center>Not-Contacted %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center>Satisfied </th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Satisfied %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied %</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					satisfied = 0.00;
					contactable = 0.00;
					contactable_perc = 0.00;
					noncontactable_perc = 0.00;
					satisfied_perc = 0.00;
					dissatisfied_perc = 0.00;
					followupcount = crs.getDouble("followupcount");
					contactable = crs.getDouble("contactable");
					noncontactable = crs.getDouble("noncontactable");
					satisfied = crs.getDouble("satisfied");
					dissatisfied = crs.getDouble("dissatisfied");

					if (contactable != 0) {
						satisfied_perc = (satisfied / contactable) * 100;
						contactable_perc = (contactable / followupcount) * 100;
					}
					if (contactable != 0) {
						dissatisfied_perc = (dissatisfied / contactable) * 100;
					}
					if (noncontactable != 0) {
						noncontactable_perc = (noncontactable / followupcount) * 100;
					}
					grandfollowupcount += followupcount;
					grandcontactable += contactable;
					grandnoncontactable += noncontactable;
					grandsatisfied += satisfied;
					granddissatisfied += dissatisfied;

					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"))
							.append("</a></td>\n");
					Str.append("<td align=right>").append(crs.getString("followupcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("contactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(contactable_perc)).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("noncontactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(noncontactable_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("satisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(satisfied_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("dissatisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(dissatisfied_perc)).append("</td>\n");

					Str.append("</tr>");

				}
				// SOP("grandfollowupcount-------" + grandfollowupcount);
				// SOP("grandcontactable-------" + grandcontactable);
				// SOP("grandnoncontactable-------" + grandnoncontactable);

				if (grandcontactable != 0) {
					grandsatisfiedperc = (grandsatisfied / grandcontactable * 100);
					grandcontactable_perc = ((grandcontactable + 0.0) / grandfollowupcount) * 100;
					// SOP("grandcontactable_perc-------" + grandcontactable_perc);
				}
				if (grandcontactable != 0) {
					granddissatisfiedperc = (granddissatisfied / grandcontactable * 100);
				}
				if (grandnoncontactable != 0)
				{
					grandnoncontactable_perc = ((grandnoncontactable + 0.0) / grandfollowupcount) * 100;
				}
				// SOP("grandnoncontactable_perc-------" + grandnoncontactable_perc);
				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(grandfollowupcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandcontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandcontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandnoncontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandnoncontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandsatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandsatisfiedperc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(granddissatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(granddissatisfiedperc)).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No CRM Follow-up found!</b></font>");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String TechnicianFollowupDetails() {
		try {
			int count = 0;
			double followupcount = 0;
			int grandfollowupcount = 0;

			double contactable = 0;
			int grandcontactable = 0;
			double contactable_perc = 0;
			double grandcontactable_perc = 0;

			double noncontactable = 0;
			int grandnoncontactable = 0;
			double noncontactable_perc = 0;
			double grandnoncontactable_perc = 0;

			double satisfied = 0;
			double grandsatisfied = 0;
			double grandsatisfiedperc = 0;
			double dissatisfied = 0;
			double granddissatisfied = 0;
			double granddissatisfiedperc = 0;
			double satisfied_perc = 0.00;
			double dissatisfied_perc = 0.00;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT emp_id, emp_name, "
					// + " COUNT(DISTINCT veh_id) AS followupcount, "
					+ " COUNT(DISTINCT jcpsf_id) AS followupcount, "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id = 1 THEN jcpsf_id END ) AS contactable, "
					+ "	COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id >= 2 THEN jcpsf_id END ) AS noncontactable, "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 1 THEN jcpsf_id END ) AS satisfied, "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 2 THEN jcpsf_id END ) AS dissatisfied"
					// + " SUM(IF(jcpsf_psffeedbacktype_id = 1,1,0)) as contactable, "
					// + " SUM(IF(jcpsf_psffeedbacktype_id >= 2,1,0)) as noncontactable, "
					// + " SUM(IF(jcpsf_satisfied = 1,1,0)) as satisfied, "
					// + " SUM(IF(jcpsf_satisfied = 2,1,0)) as dissatisfied "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_technician_emp_id "
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1";
			if (!starttime.equals("")) {
				StrSql += " AND SUBSTR(jcpsf_followup_time, 1, 8) >= SUBSTR('" + starttime + "',1,8)";
			}
			if (!endtime.equals("")) {
				StrSql += " AND SUBSTR(jcpsf_followup_time, 1, 8) <= SUBSTR('" + endtime + "',1,8)";
			}
			if (!jctype_id.equals("")) {
				StrSql += " AND jc_jctype_id in (" + jctype_id + ")";
			}
			if (emp_all_exe.equals("0"))
			{
				StrSql += ExeAccess;
			}
			StrSql += StrSearch
					+ CrmStrSearch
					+ PsfStrSearch
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// + " LIMIT 1000";

			// SOP("StrSql====Technician=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th style=\"text-align:center\" data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\" align=center>Technician</th>\n");
				Str.append("<th align=center>Total</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Contact</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Contact %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" align=center>Not-Contacted</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" align=center>Not-Contacted %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center>Satisfied </th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Satisfied %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied%</th>\n");
				Str.append("</tr>");
				Str.append("<tbody>\n");
				while (crs.next()) {
					satisfied = 0.00;
					contactable = 0.00;
					contactable_perc = 0.00;
					noncontactable_perc = 0.00;
					satisfied_perc = 0.00;
					dissatisfied_perc = 0.00;
					followupcount = crs.getDouble("followupcount");
					contactable = crs.getDouble("contactable");
					noncontactable = crs.getDouble("noncontactable");
					satisfied = crs.getDouble("satisfied");
					dissatisfied = crs.getDouble("dissatisfied");

					if (contactable != 0) {
						satisfied_perc = (satisfied / contactable) * 100;
						contactable_perc = (contactable / followupcount) * 100;
					}
					if (contactable != 0) {
						dissatisfied_perc = (dissatisfied / contactable) * 100;
					}
					if (noncontactable != 0) {
						noncontactable_perc = (noncontactable / followupcount) * 100;
					}
					grandfollowupcount += followupcount;
					grandcontactable += contactable;
					grandnoncontactable += noncontactable;
					grandsatisfied += satisfied;
					granddissatisfied += dissatisfied;
					count++;
					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"))
							.append("</a></td>\n");

					Str.append("<td align=right>").append(crs.getString("followupcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("contactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(contactable_perc)).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("noncontactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(noncontactable_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("satisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(satisfied_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("dissatisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(dissatisfied_perc)).append("</td>\n");

					Str.append("</tr>");

				}
				if (grandcontactable != 0) {
					grandsatisfiedperc = (grandsatisfied / grandcontactable * 100);
					grandcontactable_perc = ((grandcontactable + 0.0) / grandfollowupcount) * 100;
				}
				if (grandcontactable != 0) {
					granddissatisfiedperc = (granddissatisfied / grandcontactable * 100);
				}
				if (grandnoncontactable != 0)
				{
					grandnoncontactable_perc = ((grandnoncontactable + 0.0) / grandfollowupcount) * 100;
				}
				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(grandfollowupcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandcontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandcontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandnoncontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandnoncontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandsatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandsatisfiedperc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(granddissatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(granddissatisfiedperc)).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
			else {
				Str.append("<font color=red><b>No Technician found!</b></font>");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateCRM() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " from " + compdb(comp_id) + "axela_emp"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_emp_id = emp_id"
					+ " WHERE emp_active = 1"
					// + " AND emp_crm = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql--PopulateCRM----" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), crm_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n)");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateAdviser() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " from " + compdb(comp_id) + "axela_emp"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc ON jc_emp_id = emp_id"
					+ " WHERE emp_active = 1"
					// + " AND emp_service = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql--PopulateAdviser----" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), jc_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n)");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql--PopulateBranch----" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("branch_id"), jc_branch_ids));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n)");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobcardType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_jctype_id = jctype_id"
					+ " WHERE 1 = 1"
					+ " GROUP BY jctype_id"
					+ " ORDER BY jctype_name";
			// SOP("strsql==PSFDays=======" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("jctype_id"), jctype_ids));
				Str.append(">").append(crs.getString("jctype_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModels(String branch_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name "
					+ " FROM axelaauto.axela_preowned_model "
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = carmanuf_id"
					+ " WHERE 1 = 1";
			// + " AND model_active = '1'";
			if (!branch_id.equals("")) {
				StrSql += " AND preownedmodel_carmanuf_id IN (" + (branch_id) + ") ";
			}
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql-------PopulateModels------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			Str = Str.append("<select name=dr_model_id size=10 multiple=multiple class=selectbox id=dr_model_id style=\"width:250px\">\n>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=" + crs.getString("preownedmodel_id") + "");
					Str.append(ArrSelectdrop(crs.getInt("preownedmodel_id"), model_ids));
					Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
				}
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
