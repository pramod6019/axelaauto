package axela.sales;
//divya 30th may 2013
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_CRMFollowup_Status extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", comp_id = "0", branch_id = "", brand_id = "", region_id = "", soe_id = "", sob_id = "";
	public String[] team_ids, exe_ids, model_ids, crmdays_ids, crmconcern_ids, brand_ids, region_ids, branch_ids, soe_ids, sob_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "", StrClosedHTML = "", StrHTMLcrmconcern = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String EnquirySearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String crmdays_crmtype_id = "0";
	public String dr_totalby = "0";
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String StrSql = "", StrSmart = "";
	public String TeamJoin = "";
	public String emp_all_exe = "";
	public String filter = "", crmfilter = "";
	public String crmdays_id = "", crmconcern_id = "";
	public MIS_Check1 mischeck = new MIS_Check1();

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
				filter = PadQuotes(request.getParameter("filter"));
				crmfilter = PadQuotes(request.getParameter("crmfilter"));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					EnquirySearch = BranchAccess.replace("branch_id", "enquiry_branch_id") + "";

					if (!starttime.equals("")) {
						EnquirySearch = EnquirySearch + " AND SUBSTR(crm_followup_time,1,8) >= substr('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						EnquirySearch = EnquirySearch + " AND SUBSTR(crm_followup_time,1,8) <= substr('" + endtime + "',1,8)";
					}
					if (!crmdays_crmtype_id.equals("0")) {
						EnquirySearch = EnquirySearch + " AND crmdays_crmtype_id = " + crmdays_crmtype_id;
					}
					if (!exe_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("")) {
						mischeck.brand_id = brand_id;
						EnquirySearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						EnquirySearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						EnquirySearch = EnquirySearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!soe_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					if (!crmdays_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND crm_crmdays_id IN (" + crmdays_id + ")";
					}
					if (!crmconcern_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND crm_crmconcern_id IN (" + crmconcern_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						TeamJoin = " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id ";
						TeamJoin = TeamJoin + " AND team_id IN (" + team_id + ")";
					}
					// if (!emp_id.equals("1")) {
					// EnquirySearch = EnquirySearch + " AND (enquiry_emp_id = "
					// + emp_id
					// + " or enquiry_emp_id IN (select ex.empexe_emp_id "
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
						StrHTMLcrmconcern = PopulateCRMConcernDetails();
					}
				}
				// SOP("filter====" + filter);
				if (filter.equals("yes")) {
					CrmConcernSearchDetails(request, response);
				}
				if (crmfilter.equals("yes")) {
					crmDetailsRedirect(request, response);
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
		// SOP("dr_totalby=====" + dr_totalby);
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
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
		sob_id = RetrunSelectArrVal(request, "dr_sob");
		sob_ids = request.getParameterValues("dr_sob");
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
		StrSql = " SELECT crmfeedbacktype_id, crmfeedbacktype_name, count(crm_id) AS Total ";
		String CountSql = " SELECT Count(crmfeedbacktype_id)";
		String StrJoin = " FROM (SELECT crmfeedbacktype_id, crmfeedbacktype_name FROM "
				+ " " + maindb() + "sales_crm_feedbacktype "
				+ " UNION "
				+ " SELECT 0, 'Not Attempted') axela_sales_crm_feedbacktype"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_crmfeedbacktype_id = crmfeedbacktype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = crm_emp_id"
				+ TeamJoin
				+ " WHERE 1 = 1 "
				+ " AND branch_active= 1 "
				+ EnquirySearch;

		StrSql += ExeAccess.replace("emp_id", "crm_emp_id");

		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY crmfeedbacktype_id ORDER BY Total desc";
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
					chart_data = chart_data + "{'type': '" + crs.getString("crmfeedbacktype_name") + "', 'total':" + crs.getString("Total") + "}";
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
		StringBuilder multiselect = new StringBuilder();

		multiselect.append("&starttime=" + starttime);
		multiselect.append("&endtime=" + endtime);
		multiselect.append("&brand_id=" + brand_id);
		multiselect.append("&region_id=" + region_id);
		multiselect.append("&branch_id=" + branch_id);
		multiselect.append("&model_id=" + model_id);
		multiselect.append("&soe_id=" + soe_id);
		multiselect.append("&sob_id=" + sob_id);
		multiselect.append("&team_id=" + team_id);
		multiselect.append("&exe_id=" + exe_id);
		multiselect.append("&total_by=" + dr_totalby);
		multiselect.append("&crmdays_crmtype_id=" + crmdays_crmtype_id);
		multiselect.append("&crmdays_id=" + crmdays_id);
		multiselect.append("&crmconcern_id=" + crmconcern_id);

		try {
			int count = 0, crmcount = 0, ticket_count = 0, ticket_closed = 0;
			int contactable = 0, noncontactable = 0, notattempted = 0, satisfied = 0, dissatisfied = 0, crmemail = 0, booked = 0;
			String total_by = "";
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT COUNT(crm_id) as crmcount, "
					+ " COUNT(DISTINCT CASE WHEN crm_crmfeedbacktype_id = 1 THEN crm_id END ) as 'contactable',"
					+ " COUNT(DISTINCT CASE WHEN crm_crmfeedbacktype_id = 2 THEN crm_id END ) as 'noncontactable',"
					+ " COUNT(DISTINCT CASE WHEN crm_crmfeedbacktype_id = 0 THEN crm_id END ) as 'notattempted',"
					+ " COUNT(DISTINCT CASE WHEN crm_satisfied = 1 THEN crm_id END ) as 'satisfied',"
					+ " COUNT(DISTINCT CASE WHEN crm_satisfied = 2 THEN crm_id END ) as 'dissatisfied',"

					+ " COUNT(ticket_id) AS 'ticket_count',"
					+ " COUNT(DISTINCT CASE WHEN ticket_ticketstatus_id = 3 THEN ticket_id END ) AS 'ticket_closed',"

					+ " COUNT(DISTINCT CASE WHEN enquiry_status_id = 2 THEN enquiry_id END ) as 'booked',"
					+ " COUNT(DISTINCT CASE WHEN crm_crmfeedbacktype_id = 1 AND crm_email2 !='' THEN crm_id	END	) AS 'crmemail2',"
					+ " emp_id, emp_name, branch_name, branch_id, branch_region_id, branch_brand_id ";
			if (dr_totalby.equals("4")) {
				StrSql += " , region_name";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " ,brand_name";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_sales_crm "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
					// + " AND enquiry_status_id=1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket ON ticket_crm_id = crm_id";
			if (dr_totalby.equals("4")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region on region_id = branch_region_id";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " INNER JOIN axela_brand on brand_id = branch_brand_id";
			}
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = crm_emp_id"
					+ TeamJoin
					// + " AND crm_trigger > 0 AND crm_desc = ''"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ EnquirySearch
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");

			StrSql += ExeAccess.replace("emp_id", "crm_emp_id");
			StrSql += " GROUP BY ";
			if (dr_totalby.equals("1")) {
				StrSql += " emp_id";
			}
			else if (dr_totalby.equals("3")) {
				StrSql += " branch_id ";
			}
			else if (dr_totalby.equals("4")) {
				StrSql += " branch_region_id";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " branch_brand_id";
			}
			StrSql += " ORDER BY";

			if (dr_totalby.equals("1")) {
				StrSql += " emp_name";
			}
			else if (dr_totalby.equals("3")) {
				StrSql += " branch_name ";
			}
			else if (dr_totalby.equals("4")) {
				StrSql += " region_name";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " brand_name";
			}

			// SOP("StrSql-------CRMDetails---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"table \">\n");
			Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th data-hide=\"phone\">#</th>\n");
			if (dr_totalby.equals("1")) {
				Str.append("<th data-toggle=\"true\" align=center>CRM Name</th>\n");
			}
			else if (dr_totalby.equals("3")) {
				Str.append("<th data-toggle=\"true\" align=center>Branch</th>\n");
			}
			else if (dr_totalby.equals("4")) {
				Str.append("<th data-toggle=\"true\" align=center>Region</th>\n");
			}
			else if (dr_totalby.equals("5")) {
				Str.append("<th data-toggle=\"true\" align=center>Brand</th>\n");
			}

			Str.append("<th data-hide=\"phone\">Total</th>\n");
			Str.append("<th data-hide=\"phone\">Contactable</th>\n");
			Str.append("<th data-hide=\"phone\">Contactable %</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Non Contactable</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Not Attempted</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Satisfied</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Satisfied %</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Dis Satisfied</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Dis Satisfied %</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Booked </th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Email %</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Ticket Closed </th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Ticket Closed %</th>\n");
			Str.append("</tr>");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			while (crs.next()) {
				count++;
				crmcount += crs.getInt("crmcount");
				contactable += crs.getInt("contactable");
				noncontactable += crs.getInt("noncontactable");
				notattempted += crs.getInt("notattempted");
				satisfied += crs.getInt("satisfied");
				dissatisfied += crs.getInt("dissatisfied");
				crmemail += crs.getInt("crmemail2");
				booked += crs.getInt("booked");
				ticket_count += crs.getInt("ticket_count");
				ticket_closed += crs.getInt("ticket_closed");

				Str.append("<tr align=center>\n");
				Str.append("<td align=center>").append(count).append("</b></td>\n");

				if (dr_totalby.equals("1")) {
					Str.append("<td align=left>").append(crs.getString("emp_name")).append("</td>\n");
				}
				else if (dr_totalby.equals("3")) {
					Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
				}
				else if (dr_totalby.equals("4")) {
					Str.append("<td align=left>").append(crs.getString("region_name")).append("</td>\n");
				}
				else if (dr_totalby.equals("5")) {
					Str.append("<td align=left>").append(crs.getString("brand_name")).append("</td>\n");
				}
				Str.append("<td align=right>").append(crs.getInt("crmcount")).append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("contactable")).append("</td>\n");
				Str.append("<td align=right>").append(getPercentage(crs.getInt("contactable"), crs.getInt("crmcount")) + " %").append("</td>\n");

				if (dr_totalby.equals("1")) {
					total_by = "&exe_id=" + crs.getString("emp_id");
				}
				else if (dr_totalby.equals("3")) {
					total_by = "&branch_id=" + crs.getString("branch_id");
				}
				else if (dr_totalby.equals("4")) {
					total_by = "&region_id=" + crs.getString("branch_region_id");
				}
				else if (dr_totalby.equals("5")) {
					total_by = "&brand_id=" + crs.getString("branch_brand_id");
				}
				Str.append("<td align=right><a href=../sales/report-crmfollowup-status.jsp?crmfilter=yes&crm_crmfeedbacktype_id=2"
						+ total_by
						+ multiselect + ">")
						.append(crs.getInt("noncontactable")).append("</a></td>\n");

				Str.append("<td align=right><a href=../sales/report-crmfollowup-status.jsp?crmfilter=yes&crm_crmfeedbacktype_id=0"
						+ total_by
						+ multiselect + ">")
						.append(crs.getInt("notattempted")).append("</a></td>\n");

				Str.append("<td align=right>").append(crs.getInt("satisfied")).append("</td>\n");
				Str.append("<td align=right>").append(getPercentage(crs.getInt("satisfied"), crs.getInt("contactable")) + " %").append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("dissatisfied")).append("</td>\n");
				Str.append("<td align=right>").append(getPercentage(crs.getInt("dissatisfied"), crs.getInt("contactable")) + " %").append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("booked")).append("</td>\n");
				Str.append("<td align=right>").append(getPercentage(crs.getInt("crmemail2"), crs.getInt("contactable")) + " %").append("</td>\n");
				Str.append("<td align=right>").append(crs.getInt("ticket_closed")).append("</td>\n");
				Str.append("<td align=right>").append(getPercentage(crs.getInt("ticket_closed"), crs.getInt("ticket_count")) + " %").append("</td>\n");
				Str.append("</tr>");
			}
			// Str.append("<tr align=center>\n");
			Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
			Str.append("<td align=right><b>").append(crmcount).append("</b></td>\n");
			Str.append("<td align=right><b>").append(contactable).append("</b></td>\n");
			Str.append("<td align=right><b>").append(getPercentage(contactable, crmcount) + " %").append("</b></td>\n");

			Str.append("<td align=right><a href=../sales/report-crmfollowup-status.jsp?crmfilter=yes&crm_crmfeedbacktype_id=2" + multiselect + "><b>")
					.append(noncontactable).append("</b></a></td>\n");

			Str.append("<td align=right><a href=../sales/report-crmfollowup-status.jsp?crmfilter=yes&crm_crmfeedbacktype_id=0" + multiselect + "><b>")
					.append(notattempted).append("</b></a></td>\n");

			// Str.append("<td align=right><b>").append(noncontactable).append("</b></td>\n");
			// Str.append("<td align=right><b>").append(notattempted).append("</b></td>\n");
			Str.append("<td align=right><b>").append(satisfied).append("</b></td>\n");
			Str.append("<td align=right><b>").append(getPercentage(satisfied, contactable) + " %").append("</b></td>\n");
			Str.append("<td align=right><b>").append(dissatisfied).append("</b></td>\n");
			Str.append("<td align=right><b>").append(getPercentage(dissatisfied, contactable) + " %").append("</b></td>\n");
			Str.append("<td align=right><b>").append(booked).append("</b></td>\n");
			Str.append("<td align=right><b>").append(getPercentage(crmemail, contactable) + " %").append("</b></td>\n");
			Str.append("<td align=right><b>").append(ticket_closed).append("</b></td>\n");
			Str.append("<td align=right><b>").append(getPercentage(ticket_closed, ticket_count) + " %").append("</b></td>\n");
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

	public String PopulateCRMConcernDetails() throws SQLException {
		StringBuilder Str = new StringBuilder();
		String strtitle = "", strjoin = "";
		int[] concerntotal;
		int[] concerntotallink;
		int count = 0;
		int colcount = 0;
		StrSql = "SELECT crmconcern_id ,"
				+ " crmconcern_desc"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm_concern"
				+ " WHERE 1 = 1";
		if (!crmconcern_id.equals("")) {
			StrSql += " AND crmconcern_id IN (" + crmconcern_id + ")";
		}
		if (!crmdays_crmtype_id.equals("0")) {
			StrSql += " AND crmconcern_crmtype_id =" + crmdays_crmtype_id;
		}

		StrSql += " GROUP BY crmconcern_id"
				+ " ORDER BY crmconcern_desc";

		// SOP("StrSql==1111==" + StrSql);
		CachedRowSet crs1 = processQuery(StrSql, 0);

		strtitle = "SELECT ";
		if (dr_totalby.equals("1")) {
			strtitle += " emp_id, emp_name";
		}
		else if (dr_totalby.equals("3")) {
			strtitle += " branch_id , branch_name";
		}
		else if (dr_totalby.equals("4")) {
			strtitle += " branch_region_id , region_name";
		}
		else if (dr_totalby.equals("5")) {
			strtitle += " branch_brand_id , brand_name";
		}

		strjoin = " FROM " + compdb(comp_id) + "axela_emp INNER JOIN ( SELECT enquiry_emp_id";

		if (dr_totalby.equals("1")) {
			strjoin += " , crm_emp_id";
		}
		if (dr_totalby.equals("3")) {
			strjoin += " ,branch_id , branch_name";
		}
		else if (dr_totalby.equals("4")) {
			strjoin += " ,branch_region_id ,region_name";
		}
		else if (dr_totalby.equals("5")) {
			strjoin += " ,branch_brand_id ,brand_name";
		}
		// SOP("strjoin===" + strjoin);
		while (crs1.next()) {
			count++;
			strtitle += "," + "Concern" + count + " AS '" + crs1.getString("crmconcern_desc") + "'";
			strjoin += ", SUM(IF(crmconcern_id = " + crs1.getString("crmconcern_id") + ", 1, 0)) AS Concern" + count;
		}
		colcount = count;
		concerntotal = new int[colcount];
		concerntotallink = new int[colcount];
		int k = 0;
		crs1.beforeFirst();
		while (crs1.next()) {
			// for (int i = 0; i < colcount; i++) {
			concerntotallink[k++] = crs1.getInt("crmconcern_id");
			// }
		}
		// for (int i = 0; i < colcount; i++) {
		// SOP("colcount====" + concerntotallink[i]);
		// }

		strjoin += " FROM " + compdb(comp_id) + "axela_sales_crm"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_concern ON crmconcern_id = crm_crmconcern_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region on region_id = branch_region_id"
				+ " INNER JOIN axela_brand on brand_id = branch_brand_id"
				+ " WHERE 1 = 1"
				+ EnquirySearch
				+ " GROUP BY ";
		if (dr_totalby.equals("1")) {
			strjoin += " enquiry_emp_id";
		}
		else if (dr_totalby.equals("3")) {
			strjoin += " branch_id ";
		}
		else if (dr_totalby.equals("4")) {
			strjoin += " branch_region_id";
		}
		else if (dr_totalby.equals("5")) {
			strjoin += " branch_brand_id";
		}

		strjoin += ") AS tblconcern ON tblconcern.enquiry_emp_id = emp_id"
				+ " WHERE 1 = 1"
				+ " GROUP BY ";

		if (dr_totalby.equals("1")) {
			strjoin += " emp_id";
		}
		else if (dr_totalby.equals("3")) {
			strjoin += " branch_id ";
		}
		else if (dr_totalby.equals("4")) {
			strjoin += " branch_region_id";
		}
		else if (dr_totalby.equals("5")) {
			strjoin += " branch_brand_id";
		}
		StrSql = strtitle + strjoin;

		// SOP("StrSql==last==" + StrSql);

		Str.append("<div class=\"table \">\n");
		Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
		Str.append("<thead><tr>\n");
		Str.append("<th data-hide=\"phone\">#</th>\n");

		if (dr_totalby.equals("1")) {
			Str.append("<th>Counsultant</th>\n");
		}
		else if (dr_totalby.equals("3")) {
			Str.append("<th>Branch</th>\n");
		}
		else if (dr_totalby.equals("4")) {
			Str.append("<th>Region</th>\n");
		}
		else if (dr_totalby.equals("5")) {
			Str.append("<th>Brand</th>\n");
		}
		crs1.beforeFirst();
		while (crs1.next()) {
			Str.append("<th data-hide=\"phone, tablet\" align=center>");
			Str.append(crs1.getString("crmconcern_desc"));
			Str.append("</th>\n");
		}
		Str.append("<th data-hide=\"phone, tablet\" align=center>");
		Str.append("Total");
		Str.append("</th>\n");

		Str.append("</tr>");
		Str.append("</thead>\n");

		// Close the CRs for CRM concern Desc
		CachedRowSet crs2 = processQuery(StrSql, 0);
		count = 0;

		crs2.beforeFirst();
		Str.append("<tbody>\n");
		count = 0;
		while (crs2.next()) {
			crs1.beforeFirst();
			count++;
			Str.append("<tr align=center>\n");
			Str.append("<td align=center>").append(count).append("</b></td>\n");
			if (dr_totalby.equals("1")) {
				Str.append("<td align=left>").append(crs2.getString("emp_name")).append("</td>\n");
			}
			else if (dr_totalby.equals("3")) {
				Str.append("<td align=left>").append(crs2.getString("branch_name")).append("</td>\n");
			}
			else if (dr_totalby.equals("4")) {
				Str.append("<td align=left>").append(crs2.getString("region_name")).append("</td>\n");
			}
			else if (dr_totalby.equals("5")) {
				Str.append("<td align=left>").append(crs2.getString("brand_name")).append("</td>\n");
			}

			int rowtotal = 0;

			for (int i = 3; i <= colcount + 2; i++) {

				Str.append("<td align=right><b><a href=../sales/report-crmfollowup-status.jsp?filter=yes");

				if (dr_totalby.equals("1")) {
					Str.append("&emp_id=").append(crs2.getString("emp_id"));
				}
				else if (dr_totalby.equals("3")) {
					Str.append("&branch_id=").append(crs2.getString("branch_id"));
				}
				else if (dr_totalby.equals("4")) {
					Str.append("&region_id=").append(crs2.getString("branch_region_id"));
				}
				else if (dr_totalby.equals("5")) {
					Str.append("&brand_id=").append(crs2.getString("branch_brand_id"));
				}

				Str.append("&crmconcern_id=" + concerntotallink[i - 3]);
				Str.append("&starttime=" + starttime);
				Str.append("&endtime=" + endtime);
				Str.append("&brand_id=" + brand_id);
				Str.append("&region_id=" + region_id);
				Str.append("&branch_id=" + branch_id);
				Str.append("&model_id=" + model_id);
				Str.append("&team_id=" + team_id);
				Str.append("&soe_id=" + soe_id);
				Str.append("&sob_id=" + sob_id);
				Str.append("&exe_id=" + exe_id);
				Str.append("&crmdays_crmtype_id=" + crmdays_crmtype_id);
				Str.append("&crmdays_id=" + crmdays_id);
				Str.append("&crmconcern_id=" + crmconcern_id);
				Str.append(">");
				Str.append(crs2.getString(i));
				Str.append("</a>");
				Str.append("</b></td>\n");

				rowtotal += crs2.getInt(i);
				concerntotal[i - 3] += crs2.getInt(i);
			}
			Str.append("<td align=right><b>")
					.append("<a href=../sales/report-crmfollowup-status.jsp?filter=yes");
			if (dr_totalby.equals("1")) {
				Str.append("&emp_id=").append(crs2.getString("emp_id"));
			}
			else if (dr_totalby.equals("3")) {
				Str.append("&branch_id=").append(crs2.getString("branch_id"));
			}
			else if (dr_totalby.equals("4")) {
				Str.append("&region_id=").append(crs2.getString("branch_region_id"));
			}
			else if (dr_totalby.equals("5")) {
				Str.append("&brand_id=").append(crs2.getString("branch_brand_id"));
			}

			Str.append("&starttime=" + starttime);
			Str.append("&endtime=" + endtime);
			Str.append("&brand_id=" + brand_id);
			Str.append("&region_id=" + region_id);
			Str.append("&branch_id=" + branch_id);
			Str.append("&model_id=" + model_id);
			Str.append("&soe_id=" + soe_id);
			Str.append("&sob_id=" + sob_id);
			Str.append("&team_id=" + team_id);
			Str.append("&exe_id=" + exe_id);
			Str.append("&crmdays_crmtype_id=" + crmdays_crmtype_id);
			Str.append("&crmdays_id=" + crmdays_id);
			Str.append("&crmconcern_id=" + crmconcern_id);
			Str.append(">")
					.append(rowtotal)
					.append("</a>")
					.append("</b></td>\n");
			Str.append("</tr>");
		}

		Str.append("<tr align=center>\n");

		Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
		int grandtotal = 0;
		for (int i = 3; i <= colcount + 2; i++) {
			Str.append("<td align=right><b><a href=../sales/report-crmfollowup-status.jsp?filter=yes");
			Str.append("&crmconcern_id=" + concerntotallink[i - 3]);
			Str.append("&starttime=" + starttime);
			Str.append("&endtime=" + endtime);
			Str.append("&brand_id=" + brand_id);
			Str.append("&region_id=" + region_id);
			Str.append("&branch_id=" + branch_id);
			Str.append("&model_id=" + model_id);
			Str.append("&soe_id=" + soe_id);
			Str.append("&sob_id=" + sob_id);
			Str.append("&team_id=" + team_id);
			Str.append("&exe_id=" + exe_id);
			Str.append("&crmdays_crmtype_id=" + crmdays_crmtype_id);
			Str.append("&crmdays_id=" + crmdays_id);
			Str.append("&crmconcern_id=" + crmconcern_id);
			Str.append(">")
					.append(concerntotal[i - 3]).append("</a></b></td>\n");
			grandtotal += concerntotal[i - 3];
		}
		Str.append("<td align=right><b>" + grandtotal + "</b></td>\n");
		Str.append("</tr>");
		Str.append("</table>");
		Str.append("</div>");

		return Str.toString();
	}
	public String PopulateCRMType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmtype_id, crmtype_name"
					+ " FROM axela_sales_crm_type ORDER BY crmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmtype_id"), crmdays_crmtype_id));
				Str.append(">").append(crs.getString("crmtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Consultants</option>\n");
		// Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", dr_totalby)).append(">Brands</option>\n");
		// Str.append("<option value=6").append(StrSelectdrop("6", dr_totalby)).append(">Models</option>\n");
		return Str.toString();
	}

	private void CrmConcernSearchDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			// HttpSession session = request.getSession(true);
			String emp_id = PadQuotes(request.getParameter("emp_id"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String brand_id = PadQuotes(request.getParameter("brand_id"));
			String region_id = PadQuotes(request.getParameter("region_id"));
			String branch_id = PadQuotes(request.getParameter("branch_id"));
			String model_id = PadQuotes(request.getParameter("model_id"));
			String team_id = PadQuotes(request.getParameter("team_id"));
			String exe_id = PadQuotes(request.getParameter("exe_id"));
			String crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("crmdays_crmtype_id")));
			String crmdays_id = PadQuotes(request.getParameter("crmdays_id"));
			String crmconcern_id = CNumeric(PadQuotes(request.getParameter("crmconcern_id")));
			String soe_id = PadQuotes(request.getParameter("soe_id"));
			String sob_id = PadQuotes(request.getParameter("sob_id"));
			// SOP("crmconcern_id==" + brand_id);

			StrSmart += " AND enquiry_id IN (SELECT crm_enquiry_id FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id";
			StrSmart += " WHERE 1=1 ";

			if (!crmdays_crmtype_id.equals("0")) {
				StrSmart += " AND crmdays_crmtype_id = " + crmdays_crmtype_id;
			}
			if (!crmdays_id.equals("")) {
				StrSmart += " AND crmdays_id = " + crmdays_id;
			}

			if (!crmconcern_id.equals("0")) {
				StrSmart += " AND crm_crmconcern_id =" + crmconcern_id;
			}
			else {
				StrSmart += " AND crm_crmconcern_id != 0";
			}
			StrSmart += " AND SUBSTR(crm_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(crm_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";

			// SOP("StrSmart===" + StrSmart);

			// enquiry filters
			if (!emp_id.equals("")) {
				StrSmart += " AND enquiry_emp_id IN ( " + emp_id + ")";
			}

			if (!brand_id.equals("")) {
				mischeck.brand_id = brand_id;
				StrSmart += " AND branch_brand_id IN (" + brand_id + ") ";
			}

			if (!region_id.equals("")) {
				StrSmart += " AND branch_region_id IN (" + region_id + ") ";
			}

			if (!branch_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				StrSmart += " AND branch_id IN (" + branch_id + ")";
			}

			if (!soe_id.equals("")) {
				StrSmart += " AND enquiry_soe_id IN (" + soe_id + ")";
			}

			if (!sob_id.equals("")) {
				StrSmart += " AND enquiry_sob_id IN (" + sob_id + ")";
			}

			if (!team_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				mischeck.branch_id = branch_id;
				StrSmart += " AND enquiry_team_id IN (" + team_id + ")";
			}

			if (!model_id.equals("")) {
				StrSmart += TeamJoin + " AND enquiry_model_id IN ( " + model_id + ")";
			}
			// SOP("StrSmart===" + StrSmart);
			SetSession("enquirystrsql", StrSmart, request);
			response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void crmDetailsRedirect(HttpServletRequest request, HttpServletResponse response) {
		try {
			// HttpSession session = request.getSession(true);
			String emp_id = PadQuotes(request.getParameter("emp_id"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String brand_id = PadQuotes(request.getParameter("brand_id"));
			String region_id = PadQuotes(request.getParameter("region_id"));
			String branch_id = PadQuotes(request.getParameter("branch_id"));
			String model_id = PadQuotes(request.getParameter("model_id"));
			String team_id = PadQuotes(request.getParameter("team_id"));
			String exe_id = PadQuotes(request.getParameter("exe_id"));
			String crmdays_crmtype_id = PadQuotes(request.getParameter("crmdays_crmtype_id"));
			String crmdays_id = PadQuotes(request.getParameter("crmdays_id"));
			String crmconcern_id = CNumeric(PadQuotes(request.getParameter("crmconcern_id")));
			String crmfeedbacktype_id = PadQuotes(request.getParameter("crm_crmfeedbacktype_id"));
			// SOP("crmconcern_id==" + brand_id);
			String soe_id = PadQuotes(request.getParameter("soe_id"));
			String sob_id = PadQuotes(request.getParameter("sob_id"));

			StrSmart += " AND enquiry_id IN (SELECT crm_enquiry_id FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id";
			StrSmart += " WHERE 1=1 "
					+ " AND crm_emp_id != 0";

			if (!exe_id.equals("")) {
				StrSmart += " AND crm_emp_id = " + exe_id;
			}

			if (!crmfeedbacktype_id.equals("")) {
				StrSmart += " AND crm_crmfeedbacktype_id = " + crmfeedbacktype_id;
			}
			if (!crmdays_crmtype_id.equals("")) {
				StrSmart += " AND crmdays_crmtype_id = " + crmdays_crmtype_id;
			}

			if (!crmdays_id.equals("")) {
				StrSmart += " AND crmdays_id = " + crmdays_id;
			}

			if (!crmconcern_id.equals("0")) {
				StrSmart += " AND crm_crmconcern_id =" + crmconcern_id;
			}
			// else {
			// StrSmart += " AND crm_crmconcern_id != 0";
			// }
			StrSmart += " AND SUBSTR(crm_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(crm_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";

			// SOP("StrSmart===" + StrSmart);

			// enquiry filters
			if (!emp_id.equals("")) {
				StrSmart += " AND enquiry_emp_id IN ( " + emp_id + ")";
			}

			if (!brand_id.equals("")) {
				mischeck.brand_id = brand_id;
				StrSmart += " AND branch_brand_id IN (" + brand_id + ") ";
			}

			if (!region_id.equals("")) {
				StrSmart += " AND branch_region_id IN (" + region_id + ") ";
			}

			if (!branch_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				StrSmart += " AND branch_id IN (" + branch_id + ")";
			}

			if (!soe_id.equals("")) {
				StrSmart += " AND enquiry_soe_id IN (" + soe_id + ")";
			}

			if (!sob_id.equals("")) {
				StrSmart += " AND enquiry_sob_id IN (" + sob_id + ")";
			}

			if (!team_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				mischeck.branch_id = branch_id;
				StrSmart += " AND enquiry_team_id IN (" + team_id + ")";
			}

			if (!model_id.equals("")) {
				StrSmart += TeamJoin + " AND enquiry_model_id IN ( " + model_id + ")";
			}
			// SOP("StrSmart===" + StrSmart);
			SetSession("enquirystrsql", StrSmart, request);
			response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateSoe() {
		String sb = "";
		try
		{
			StrSql = " SELECT soe_id, soe_name "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id=model_id"
					+ " ORDER BY soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				sb = sb + "<option value=" + crs.getString("soe_id") + "";
				sb = sb + ArrSelectdrop(crs.getInt("soe_id"), soe_ids);
				sb = sb + ">" + crs.getString("soe_name") + "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return sb;
	}

	public String PopulateSob(String soe_id, String comp_id, HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try
		{
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1";
			if (!soe_id.equals("")) {
				StrSql += " AND soetrans_soe_id IN (" + soe_id + ")";
			}
			StrSql += " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("SOB===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			sb.append("<select name='dr_sob' id='dr_sob' multiple='multiple' class='form-control multiselect-dropdown'>");
			while (crs.next()) {
				sb.append("<option value=").append(crs.getString("sob_id")).append("");
				sb.append(ArrSelectdrop(crs.getInt("sob_id"), sob_ids));
				sb.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			sb.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return sb.toString();
	}
}
