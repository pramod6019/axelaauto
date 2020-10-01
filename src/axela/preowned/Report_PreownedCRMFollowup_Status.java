package axela.preowned;
//divya 30th may 2013
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_PreownedCRMFollowup_Status extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", comp_id = "0", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, crmdays_ids, crmconcern_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "", StrClosedHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String EnquirySearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String precrmfollowupdays_precrmtype_id = "0";
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String StrSql = "";
	public String TeamJoin = "";
	public String emp_all_exe = "";
	// public String crmfollowupdays_id = "";
	public String crmdays_id = "", crmconcern_id = "";
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					EnquirySearch = BranchAccess.replace("branch_id", "preowned_branch_id") + "";

					if (!starttime.equals("")) {
						EnquirySearch = EnquirySearch + " AND precrmfollowup_followup_time >= substr('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						EnquirySearch = EnquirySearch + " AND precrmfollowup_followup_time <= substr('" + endtime + "',1,8)";
					}
					if (!precrmfollowupdays_precrmtype_id.equals("0")) {
						EnquirySearch = EnquirySearch + " AND precrmfollowupdays_precrmtype_id = " + precrmfollowupdays_precrmtype_id;
					}
					if (!exe_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND preowned_emp_id IN (" + exe_id + ")";
					}
					// if (!brand_id.equals("")) {
					// EnquirySearch += " AND branch_brand_id IN (" + brand_id + ") ";
					// }
					if (!region_id.equals("")) {
						EnquirySearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND preowned_preownedmodel_id IN (" + model_id + ")";
					}
					if (!crmdays_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND precrmfollowup_precrmfollowupdays_id IN (" + crmdays_id + ")";
					}
					if (!crmconcern_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND precrmfollowup_precrmconcern_id IN (" + crmconcern_id + ")";
					}
					// if (!team_id.equals("")) {
					// TeamJoin = " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id "
					// + " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id ";
					// TeamJoin = TeamJoin + " AND team_id IN (" + team_id + ")";
					// }
					// if (!emp_id.equals("1")) {
					// EnquirySearch = EnquirySearch + " AND (preowned_emp_id = "
					// + emp_id
					// + " or preowned_emp_id IN (select ex.empexe_emp_id "
					// + " from " + compdb(comp_id) +
					// "axela_emp_exe AS ex where ex.empexe_emp_id=" + emp_id +
					// "))";
					// }
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						PreparePieChart();
						StrHTML = CRMDetails();
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
		precrmfollowupdays_precrmtype_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowupdays_precrmtype_id")));
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");

		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		crmdays_id = RetrunSelectArrVal(request, "dr_crmdays_id");
		crmdays_ids = request.getParameterValues("dr_crmdays_id");
		crmconcern_id = RetrunSelectArrVal(request, "dr_crmconcern_id");
		crmconcern_ids = request.getParameterValues("dr_crmconcern_id");
		// SOP("crmdays_id----------" + crmdays_id);
		// SOP("crmdays_ids----------" + crmdays_ids);
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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String PreparePieChart() {
		StrSql = " SELECT precrmfeedbacktype_id, precrmfeedbacktype_name, count(precrmfollowup_id) AS Total ";
		String CountSql = " SELECT Count(precrmfeedbacktype_id)";
		String StrJoin = " FROM (SELECT precrmfeedbacktype_id, precrmfeedbacktype_name FROM "
				+ " " + compdb(comp_id) + "axela_preowned_crmfeedbacktype "
				+ " UNION "
				+ " SELECT 0, 'Not Attempted') axela_preowned_crmfeedbacktype"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowup ON precrmfollowup_precrmfeedbacktype_id = precrmfeedbacktype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preowned_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
				+ TeamJoin
				+ " WHERE 1 = 1 "
				+ " AND branch_active= 1 "
				+ EnquirySearch;

		StrSql += ExeAccess.replace("emp_id", "precrmfollowup_crm_emp_id");

		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY precrmfeedbacktype_id ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		// SOP("prepare pi chart=====" + StrSql);
		int count = 0;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				chart_data = "[";
				while (crs.next()) {
					count++;
					// chart_data = chart_data + "['" +
					// crs.getString("crmfeedbacktype_name") + " (" +
					// crs.getString("Total") + ")'," + crs.getString("Total") +
					// "]";
					chart_data = chart_data + "{'type': '" + crs.getString("precrmfeedbacktype_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total = chart_data_total + crs.getInt("Total");
					if (count < TotalRecords) {
						chart_data = chart_data + ",";
					} else {
					}
				}
				chart_data = chart_data + "]";
			} else {
				NoChart = "<font color=red><b>No CRM Follow-up Found!</b></font>";
			}
			crs.close();
		} catch (SQLException ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return "";
	}

	public String CRMDetails() {
		try {
			int count = 0, precrmfollowupcount = 0;
			int contactable = 0, noncontactable = 0, notattempted = 0, satisfied = 0, dissatisfied = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT COUNT(precrmfollowup_id) as precrmfollowupcount, "
					+ " COUNT(DISTINCT CASE WHEN precrmfollowup_precrmfeedbacktype_id = 1 THEN precrmfollowup_id END ) as 'contactable',"
					+ " COUNT(DISTINCT CASE WHEN precrmfollowup_precrmfeedbacktype_id = 2 THEN precrmfollowup_id END ) as 'noncontactable',"
					+ " COUNT(DISTINCT CASE WHEN precrmfollowup_precrmfeedbacktype_id = 0 THEN precrmfollowup_id END ) as 'notattempted',"
					+ " COUNT(DISTINCT CASE WHEN precrmfollowup_satisfied = 1 THEN precrmfollowup_id END ) as 'satisfied',"
					+ " COUNT(DISTINCT CASE WHEN precrmfollowup_satisfied = 2 THEN precrmfollowup_id END ) as 'dissatisfied',"
					+ " emp_id, emp_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = precrmfollowup_preowned_id"
					// + " AND preowned_preownedstatus_id=1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = precrmfollowup_crm_emp_id"
					+ TeamJoin
					// + " AND precrmfollowup_trigger > 0 AND precrmfollowup_desc = ''"
					+ " WHERE 1=1 AND branch_active = 1 "
					+ EnquirySearch
					+ BranchAccess.replace("branch_id", "preowned_branch_id");

			StrSql += ExeAccess.replace("emp_id", "precrmfollowup_crm_emp_id");

			StrSql += " GROUP BY emp_id ";
			SOP("StrSql-------CRMDetails---------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th data-hide=\"phone\">#</th>\n");
			Str.append("<th data-toggle=\"true\" align=center>CRM Name</th>\n");
			Str.append("<th data-hide=\"phone\">Total</th>\n");
			Str.append("<th data-hide=\"phone\">Contactable</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Non Contactable</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Not Attempted</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Satisfied</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Dis Satisfied</th>\n");
			Str.append("</tr>");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			while (crs.next()) {
				count++;
				precrmfollowupcount += crs.getInt("precrmfollowupcount");
				contactable += crs.getInt("contactable");
				noncontactable += crs.getInt("noncontactable");
				notattempted += crs.getInt("notattempted");
				satisfied += crs.getInt("satisfied");
				dissatisfied += crs.getInt("dissatisfied");

				Str.append("<tr align=center>\n");
				Str.append("<td align=center>").append(count).append("</b></td>\n");
				Str.append("<td align=left>").append(crs.getString("emp_name")).append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("precrmfollowupcount")).append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("contactable")).append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("noncontactable")).append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("notattempted")).append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("satisfied")).append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("dissatisfied")).append("</td>\n");
				Str.append("</tr>");
			}
			// Str.append("<tr align=center>\n");
			Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
			Str.append("<td align=right><b>").append(precrmfollowupcount).append("</b></td>\n");
			Str.append("<td align=right><b>").append(contactable).append("</b></td>\n");
			Str.append("<td align=right><b>").append(noncontactable).append("</b></td>\n");
			Str.append("<td align=right><b>").append(notattempted).append("</b></td>\n");
			Str.append("<td align=right><b>").append(satisfied).append("</b></td>\n");
			Str.append("<td align=right><b>").append(dissatisfied).append("</b></td>\n");
			// Str.append("</tr>");
			Str.append("</table>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreowedCRMType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT precrmtype_id, precrmtype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_type ORDER BY precrmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("precrmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmtype_id"), precrmfollowupdays_precrmtype_id));
				Str.append(">").append(crs.getString("precrmtype_name")).append("</option>\n");
			}
			crs.close();
			// SOP("option===" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
