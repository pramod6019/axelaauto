package axela.sales;

//Saiman 16th feb 2013
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Campaign_Dash extends Connect {

	public String submitB = "";
	public String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, branch_ids, region_ids;
	public String team_id = "", exe_id = "0", model_id = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "", StrClosedHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String EnquirySearch = "";
	public String ExeAccess = "";
	public String emp_all_exe = "", filter = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	HttpSession session = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				filter = PadQuotes(request.getParameter("filter"));
				GetValues(request, response);
				CheckForm();
				EnquirySearch = BranchAccess + ExeAccess;
				if (filter.equals("yes"))
				{
					CampaignDetails(request, response);
				}
				if (!starttime.equals("")) {
					EnquirySearch = EnquirySearch + " AND enquiry_date >= SUBSTR('" + starttime + "',1,8)";
				}
				if (!endtime.equals("")) {
					EnquirySearch = EnquirySearch + " AND enquiry_date <= SUBSTR('" + endtime + "',1,8)";
				}
				if (!exe_id.equals("")) {
					EnquirySearch = EnquirySearch + " AND enquiry_emp_id IN(" + exe_id + ")";

				}
				if (!brand_id.equals("")) {
					EnquirySearch += " AND branch_brand_id IN (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					EnquirySearch += " AND branch_region_id IN (" + region_id + ") ";
				}
				if (!branch_id.equals("")) {
					mischeck.exe_branch_id = branch_id;
					EnquirySearch += " AND branch_id IN (" + branch_id + ")";
				}
				if (!model_id.equals("")) {
					EnquirySearch = EnquirySearch + " AND enquiry_model_id IN (" + model_id + ")";
				}
				if (!team_id.equals("")) {
					mischeck.exe_branch_id = branch_id;
					mischeck.branch_id = branch_id;
					EnquirySearch = EnquirySearch + " AND team_id IN (" + team_id + ")";
				}
				if (!emp_id.equals("1")) {
					// EnquirySearch = EnquirySearch + " AND (enquiry_emp_id = " + emp_id
					// + " OR enquiry_emp_id IN (select ex.empexe_emp_id "
					// + " FROM " + compdb(comp_id) + "axela_emp_exe AS ex WHERE ex.empexe_emp_id=" + emp_id + "))";
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					StrHTML = EnquirySummary();
					StrClosedHTML = EnquiryClosedSummary();
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String EnquirySummary() {
		StringBuilder Str = new StringBuilder();

		String StrStage = "";
		String StrClose = "";
		// int totalbudget = 0;
		// NumberFormat nf = NumberFormat.getInstance();
		try {
			StrSql = "SELECT CONCAT('COALESCE(SUM(IF(stage_name=''',stage_name,''',1,0)),0) AS \"',stage_name,'\"') AS stage_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage ORDER BY stage_rank";
			ResultSet rsstage = processQuery(StrSql, 0);
			while (rsstage.next()) {
				StrStage = StrStage + " " + rsstage.getString("stage_name") + ", ";
			}
			rsstage.close();
			StrSql = "SELECT CONCAT('COALESCE(SUM(IF(status_name=''',status_name,''',1,0)),0) AS \"',status_name,'\"') AS status_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status ORDER BY status_id";
			ResultSet rsclose = processQuery(StrSql, 0);
			while (rsclose.next()) {
				StrClose = StrClose + " " + rsclose.getString("status_name") + ", ";
			}
			rsclose.close();
			StrClose = StrClose.substring(0, StrClose.length() - 2);
			StrSql = " (SELECT CONCAT(campaign_name,' <br>(',date_format(campaign_startdate,'%d/%m/%Y'),'-',date_format(campaign_enddate,'%d/%m/%Y'),')') AS Campaign, "
					+ " COALESCE(format((campaign_budget),0),0) AS Budget, " + StrStage
					+ " COUNT(enquiry_id) AS Total, campaign_id, " + StrClose
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_campaign_id=campaign_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id=enquiry_stage_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id=enquiry_status_id "
					+ " WHERE 1=1 " + EnquirySearch + ""
					+ " GROUP BY campaign_id ORDER BY Total, campaign_name) "
					+ " union "
					+ " (SELECT 'Total:' AS Campaign, "
					+ " COALESCE(format(sum(campaign_budget),0),0) AS Budget, " + StrStage
					+ " COUNT(enquiry_id) AS Total, campaign_id," + StrClose
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_campaign_id=campaign_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id=enquiry_stage_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id=enquiry_status_id "
					+ " WHERE 1=1 " + EnquirySearch + ") "
					+ " ORDER BY Total, Campaign ";
			// SOP("StrSql in EnquirySummary==========" + StrSqlBreaker(StrSql));
			ResultSet rs = processQuery(StrSql, 0);
			if (rs.isBeforeFirst()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				for (int i = 1; i <= numberOfColumns; i++) {
					if (!rsmd.getColumnName(i).equals("campaign_id")) {
						Str.append("<th data-toggle=\"true\">" + rsmd.getColumnName(i) + "</th>");
					}
				}
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				rs.last();
				int rowcount = rs.getRow();
				int count = 0;
				rs.beforeFirst();
				while (rs.next()) {
					Str.append("<tr>");
					count++;
					for (int i = 1; i <= numberOfColumns; i++) {
						if (i == 1) {
							Str.append("<td");
							if (rs.getString(i).equals("Total:")) {
								Str.append(" align=right");
							}
							Str.append(" ><b>").append(rs.getString(i)).append("</b></td>");
						} else if (rsmd.getColumnName(i).equals("Budget")) {
							Str.append("<td align=right><b>" + rs.getString(i) + "</b></td>");
						} else if (rsmd.getColumnName(i).equals("Total")) {
							Str.append("<td align=right><b><a href=../sales/report-campaign-dash.jsp?filter=yes&campaign_id=")
									.append(rs.getString("campaign_id") + "&")
									.append(rsmd.getColumnName(i).replace(" ", ""))
									.append("=yes&starttime=" + starttime + "&endtime=" + endtime + "&campaignbrand_id=" + brand_id + "&campaignregion_id=" + region_id +
											"&campaignbranch_id=" + branch_id + "&campaignteam_id=" + team_id + "&campaignemp_id=" + exe_id
											+ "&campaignmodel_id=" + model_id + " target=_blank><b>").append(rs.getString(i))
									.append("</a></b></td>");

						} else if (rsmd.getColumnName(i).equals("Open")) {
							Str.append("<td align=right><b><a href=../sales/report-campaign-dash.jsp?filter=yes&campaign_id=")
									.append(rs.getString("campaign_id") + "&")
									.append(rsmd.getColumnName(i).replace(" ", ""))
									.append("=yes&starttime=" + starttime + "&endtime=" + endtime + "&campaignbrand_id=" + brand_id + "&campaignregion_id=" + region_id +
											"&campaignbranch_id=" + branch_id + "&campaignteam_id=" + team_id + "&campaignemp_id=" + exe_id
											+ "&campaignmodel_id=" + model_id + " target=_blank><font color=blue>").append(rs.getString(i))
									.append("</font></a></b></td>");
						} else if (count == rowcount) {
							if (!rsmd.getColumnName(i).equals("campaign_id")) {
								Str.append("<td align=right><a href=../sales/report-campaign-dash.jsp?filter=yes&")
										.append(rsmd.getColumnName(i).replace(" ", ""))
										.append("=yes&starttime=" + starttime + "&endtime=" + endtime + "&campaignbrand_id=" + brand_id + "&campaignregion_id=" + region_id +
												"&campaignbranch_id=" + branch_id + "&campaignteam_id=" + team_id + "&campaignemp_id=" + exe_id
												+ "&campaignmodel_id=" + model_id + " target=_blank><b>").append(rs.getString(i))
										.append("</a></b></td>");
							}
						} else {
							if (!rsmd.getColumnName(i).equals("campaign_id")) {
								Str.append("<td align=right><a href=../sales/report-campaign-dash.jsp?filter=yes&campaign_id=")
										.append(rs.getString("campaign_id") + "&")
										.append(rsmd.getColumnName(i).replace(" ", ""))
										.append("=yes&starttime=" + starttime + "&endtime=" + endtime + "&campaignbrand_id=" + brand_id + "&campaignregion_id=" + region_id +
												"&campaignbranch_id=" + branch_id + "&campaignteam_id=" + team_id + "&campaignemp_id=" + exe_id
												+ "&campaignmodel_id=" + model_id + " target=_blank><b>").append(rs.getString(i))
										.append("</a></b></td>");
							}
						}
					}
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");

			} else {
				Str.append("No Enquiry found!");
			}
			rs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error EnquirySummary: " + ex);
			return "";
		}
		return Str.toString();
	}

	public String EnquiryClosedSummary() {
		StringBuilder Str = new StringBuilder();

		String StrStage = "";
		String StrClose = "";
		// int totalbudget = 0;
		// NumberFormat nf = NumberFormat.getInstance();
		try {
			StrSql = "SELECT CONCAT('COALESCE(SUM(IF(stage_name=''',stage_name,''',1,0)),0) AS \"',stage_name,'\"') AS stage_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage ORDER BY stage_rank";
			ResultSet rsstage = processQuery(StrSql, 0);
			while (rsstage.next()) {
				StrStage = StrStage + " " + rsstage.getString("stage_name") + ", ";
			}
			rsstage.close();
			StrSql = "SELECT CONCAT('COALESCE(SUM(IF(status_name=''',status_name,''',1,0)),0) AS \"',status_name,'\"') AS status_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status WHERE status_id > 2 ORDER BY status_id";
			ResultSet rsclose = processQuery(StrSql, 0);
			while (rsclose.next()) {
				StrClose = StrClose + " " + rsclose.getString("status_name") + ", ";
			}
			rsclose.close();
			StrClose = StrClose.substring(0, StrClose.length() - 2);
			StrSql = " (SELECT CONCAT(campaign_name,' <br>(',date_format(campaign_startdate,'%d/%m/%Y'),'-',date_format(campaign_enddate,'%d/%m/%Y'),')') AS Campaign, "
					+ " COALESCE(format((campaign_budget),0),0) AS Budget, " + StrStage
					+ " COUNT(enquiry_id) AS Total, " + StrClose
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ "INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_campaign_id=campaign_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id=enquiry_stage_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id=enquiry_status_id "
					+ " WHERE 1=1 AND enquiry_status_id > 2 " + EnquirySearch + ""
					+ " GROUP BY campaign_id ORDER BY Total, campaign_name) "
					+ " UNION "
					+ " (SELECT 'Total:' AS Campaign, "
					+ " COALESCE(format(sum(campaign_budget),0),0) AS Budget, " + StrStage
					+ " COUNT(enquiry_id) AS Total, " + StrClose
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ "INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_campaign_id=campaign_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id=enquiry_stage_id "
					+ "INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id=enquiry_status_id "
					+ " WHERE 1=1 AND enquiry_status_id > 2 " + EnquirySearch + ") "
					+ " ORDER BY Total, Campaign ";
			// SOP("EnquiryClosedSummary-------------" + StrSqlBreaker(StrSql));
			ResultSet rs = processQuery(StrSql, 0);
			if (rs.isBeforeFirst()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				for (int i = 1; i <= numberOfColumns; i++) {
					Str.append("<th data-toggle=\"true\">" + rsmd.getColumnName(i) + "</th>");
				}
				Str.append("</tr>");
				rs.last();
				int rowcount = rs.getRow();
				int count = 0;
				rs.beforeFirst();
				while (rs.next()) {
					Str.append("<tr>");
					count++;
					for (int i = 1; i <= numberOfColumns; i++) {
						if (i == 1) {
							Str.append("<td");
							if (rs.getString(i).equals("Total:")) {
								Str.append(" align=right");
							}
							Str.append(" ><b>").append(rs.getString(i)).append("</b></td>");
						} else if (rsmd.getColumnName(i).equals("Total")) {
							Str.append("<td align=right><b>" + rs.getString(i) + "</b></td>");
						} else if (rsmd.getColumnName(i).equals("Open")) {
							Str.append("<td align=right><b><font color=blue>" + rs.getString(i) + "</font></b></td>");
						} else if (count == rowcount) {
							Str.append("<td align=right><b>" + rs.getString(i) + "</b></td>");
						} else {
							Str.append("<td align=right>" + rs.getString(i) + "</td>");
						}
					}
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("No Enquiry found!");
			}
			rs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void CampaignDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			String campaignbrand_id = "", campaignregion_id = "", campaignbranch_id = "", campaignteam_id = "", campaignemp_id = "", campaignmodel_id = "";
			String OpprSearch = "", campaign_id = "0", total = "";
			campaignbrand_id = PadQuotes(request.getParameter("campaignbrand_id"));
			campaignregion_id = PadQuotes(request.getParameter("campaignregion_id"));
			campaignbranch_id = PadQuotes(request.getParameter("campaignbranch_id"));
			campaignteam_id = PadQuotes(request.getParameter("campaignteam_id"));
			campaignemp_id = PadQuotes(request.getParameter("campaignemp_id"));
			campaignmodel_id = PadQuotes(request.getParameter("campaignmodel_id"));
			campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));

			String New = PadQuotes(request.getParameter("New"));
			String qualification = PadQuotes(request.getParameter("Qualification"));
			String demonstration = PadQuotes(request.getParameter("Demonstration"));
			String quotation = PadQuotes(request.getParameter("Quotation"));
			String ordersigned = PadQuotes(request.getParameter("OrderSigned"));
			String delivered = PadQuotes(request.getParameter("Delivered"));
			total = PadQuotes(request.getParameter("Total"));
			String open = PadQuotes(request.getParameter("Open"));
			String closedwon = PadQuotes(request.getParameter("ClosedWon"));
			String closedlost = PadQuotes(request.getParameter("ClosedLost"));
			String closedother = PadQuotes(request.getParameter("ClosedOthers"));

			// SOP("campaignteam_id---------" + campaignteam_id);
			// SOP("campaignemp_id---------" + campaignemp_id);
			// SOP("campaignmodel_id---------" + campaignmodel_id);
			OpprSearch = " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "' , 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "' , 1, 8)";
			if (!campaign_id.equals("0")) {
				OpprSearch += " AND enquiry_campaign_id =" + campaign_id;
			}
			if (!campaignbrand_id.equals("")) {
				OpprSearch += " AND branch_brand_id IN (" + campaignbrand_id + ")";
			}
			if (!campaignregion_id.equals("")) {
				OpprSearch += " AND branch_region_id IN (" + campaignregion_id + ")";
			}
			if (!campaignbranch_id.equals("")) {
				OpprSearch += " AND enquiry_branch_id IN (" + campaignbranch_id + ")";
			}
			if (!campaignteam_id.equals("")) {
				OpprSearch += " AND emp_id in (SELECT teamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " WHERE teamtrans_team_id IN (" + campaignteam_id + "))";
			}
			if (!campaignemp_id.equals("")) {
				OpprSearch += " AND enquiry_emp_id IN (" + campaignemp_id + ")";
			}
			if (!campaignmodel_id.equals("")) {
				OpprSearch += " AND item_model_id IN (" + campaignmodel_id + ")";
			}
			if (New.equals("yes")) {
				OpprSearch += " AND enquiry_stage_id = 1 ";
			}
			if (qualification.equals("yes")) {
				OpprSearch += " AND enquiry_stage_id = 2 ";
			}
			if (demonstration.equals("yes")) {
				OpprSearch += " AND enquiry_stage_id = 3 ";
			}
			if (quotation.equals("yes")) {
				OpprSearch += " AND enquiry_stage_id = 4 ";
			}
			if (ordersigned.equals("yes")) {
				OpprSearch += " AND enquiry_stage_id = 5 ";
			}
			if (delivered.equals("yes")) {
				OpprSearch += " AND enquiry_stage_id = 6 ";
			}
			if (total.equals("yes")) {
				OpprSearch += " AND enquiry_stage_id IN (1,2,3,4,5,6) ";
			}
			if (open.equals("yes")) {
				OpprSearch += " AND enquiry_status_id = 1 ";
			}
			if (closedwon.equals("yes")) {
				OpprSearch += " AND enquiry_status_id = 2 ";
			}
			if (closedlost.equals("yes")) {
				OpprSearch += " AND enquiry_status_id = 3 ";
			}
			if (closedother.equals("yes")) {
				OpprSearch += " AND enquiry_status_id = 4 ";
			}
			// SOP("OpprSearch------------" + OpprSearch);
			SetSession("enquirystrsql", OpprSearch, request);
			response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
