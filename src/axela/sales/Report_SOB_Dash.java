package axela.sales;
//Saiman 16th feb 2013
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_SOB_Dash extends Connect {

	public String submitB = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "", StrClosedHTML = "", Strhtml = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String EnquirySearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "", filter = "", conversion = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public String StrSearch = "", StrFilter = "";
	public String SearchURL = "report-sob-dash.jsp?";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("branch_id===="+branch_id);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				filter = PadQuotes(request.getParameter("filter"));
				if (filter.equals("yes")) {
					SoeDetails(request, response);
				}

				conversion = PadQuotes(request.getParameter("conversion"));
				if (conversion.equals("yes")) {
					ConversionDetails(request, response);
				}

				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					EnquirySearch = BranchAccess + ExeAccess;

					if (!starttime.equals("")) {
						EnquirySearch = EnquirySearch
								+ " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						EnquirySearch = EnquirySearch
								+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!exe_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("")) {
						EnquirySearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						EnquirySearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						EnquirySearch = EnquirySearch + " AND enquiry_branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						EnquirySearch = EnquirySearch + " AND teamtrans_team_id IN (" + team_id + ")";
					}
					// if (!emp_id.equals("1")) {
					// EnquirySearch = EnquirySearch + " AND (enquiry_emp_id = "
					// + emp_id
					// + " OR enquiry_emp_id in (select ex.empexe_emp_id "
					// + " from " + compdb(comp_id) +
					// "axela_emp_exe AS ex WHERE ex.empexe_emp_id=" + emp_id +
					// "))";
					// }
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = EnquirySummary();
						StrClosedHTML = EnquiryClosedSummary();
						Strhtml = ConversionSummary();
						PreparePieChart();
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
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
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
		HashMap<String, Integer> stagename = new LinkedHashMap<String, Integer>();
		HashMap<String, Integer> statusname = new LinkedHashMap<String, Integer>();

		StringBuilder multiSelect = new StringBuilder();
		multiSelect.append("&starttime=").append(starttime)
				.append("&endtime=").append(endtime)
				.append("&brand_id=").append(brand_id)
				.append("&region_id=").append(region_id)
				.append("&branch_id=").append(branch_id)
				.append("&model_id=").append(model_id)
				.append("&team_id=").append(team_id)
				.append("&exe_id=").append(exe_id)
				.append(" target=_blank");

		// int totalbudget = 0;
		// NumberFormat nf = NumberFormat.getInstance();
		try {
			StrSql = "SELECT stage_name, stage_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage"
					+ " ORDER BY stage_rank";
			// SOP("StrSql---stage----" + StrSql);
			ResultSet rsstage = processQuery(StrSql, 0);
			while (rsstage.next()) {
				StrStage += " COALESCE (SUM(IF(stage_name = '" + rsstage.getString("stage_name")
						+ "', 1, 0)), 0) AS '" + rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id") + "', ";

				// StrStage = StrStage + " " + rsstage.getString("stage_name") + ", ";
				stagename.put((rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id")), 0);
			}
			// SOP("stagename-----" + stagename.toString());
			StrSql = "SELECT status_name, status_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status"
					+ " ORDER BY status_id";
			// SOP("StrSql---close----" + StrSql);
			ResultSet rsclose = processQuery(StrSql, 0);
			while (rsclose.next()) {
				StrClose += "  COALESCE (SUM(IF(status_name = '" + rsclose.getString("status_name")
						+ "', 1, 0)), 0) AS '" + rsclose.getString("status_name") + ":" + rsclose.getString("status_id") + "', ";

				// StrClose = StrClose + " " + rsclose.getString("status_name") + ", ";
				statusname.put((rsclose.getString("status_name") + ":" + rsclose.getString("status_id")), 0);
			}
			// SOP("statusname-----" + statusname.toString());
			StrClose = StrClose.substring(0, StrClose.length() - 2);
			StrSql = " SELECT sob_id, sob_name AS SOB, " + StrStage
					+ " COUNT(DISTINCT enquiry_id) AS Total, " + StrClose
					+ " FROM " + compdb(comp_id) + "axela_sob "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_sob_id = sob_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id = enquiry_stage_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id = enquiry_status_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id "
					+ " WHERE 1 = 1 "
					+ EnquirySearch + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY SOB ";
			// SOP("StrSql-------1----s---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				RowSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				// Str.append("<div class=\"\">\n");
				Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				for (int i = 2; i <= numberOfColumns; i++) {
					Str.append("<th data-toggle=\"true\">").append(rsmd.getColumnLabel(i).split(":")[0]).append("</th>");
				}
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				int rowcount = crs.getRow();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					String sob_id = crs.getString("sob_id");
					Str.append("<tr>");
					count++;

					rsstage.beforeFirst();
					while (rsstage.next()) {
						stagename.put(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"), stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id") + "")
								+ crs.getInt(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id")));
					}

					rsclose.beforeFirst();
					while (rsclose.next()) {
						statusname.put(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"),
								statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id") + "")
										+ crs.getInt(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")));
					}

					for (int i = 2; i <= numberOfColumns; i++) {
						// SOP("NOOFCOLUMNS");

						if (i == 2) {
							Str.append("<td><b>").append(crs.getString(i)).append("</b></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Total")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&total=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + "><b>").append(crs.getString(i)).append("</b></a></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Open")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&open=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + "><b><font color=blue>").append(crs.getString(i)).append("</font></b></a></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Closed Won")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedwon=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Closed Lost")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedlost=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Closed Others")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedothers=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");
						}
						// SOP("NOOFCOLUMNS"+Str.toString());
						else if (count == rowcount) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsmd.getColumnName(i).split(":")[1]);
							Str.append("&sob_id=" + sob_id + "&status_id=" + i).append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");

						} else {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsmd.getColumnName(i).split(":")[1]);
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");

						}
					}
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");

				int totalstage = 0;
				rsstage.beforeFirst();
				while (rsstage.next()) {

					Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsstage.getString("stage_id")).append(multiSelect + "><b>")
							.append(stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"))).append("</b></a></td>\n");

					totalstage += stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"));
				}

				Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes").append(multiSelect + "><b>")
						.append(totalstage).append("</b></a></td>\n");

				rsclose.beforeFirst();
				while (rsclose.next()) {
					if (rsclose.getString("status_name").equals("Open")) {

						// Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsstage.getString("stage_id")).append(multiSelect + "><b>")
						// .append(stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"))).append("</b></a></td>\n");

						Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&open=yes");
						Str.append(multiSelect + "><b><font color=blue>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")))
								.append("<font></b></a></td>");

						// Str.append("<td align=right><b><font color=blue>").append(statusname.get(rsclose.getString("status_name") + ":" +
						// rsclose.getString("status_id"))).append("</font></b></td>\n");
					}
					if (rsclose.getString("status_name").equals("Closed Won")) {

						Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedwon=yes");
						Str.append(multiSelect + "><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")))
								.append("</b></a></td>");

						// Str.append("<td align=right><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))).append("</b></td>\n");
					}
					if (rsclose.getString("status_name").equals("Closed Lost")) {

						Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedlost=yes");
						Str.append(multiSelect + "><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")))
								.append("</b></a></td>");

						// Str.append("<td align=right><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))).append("</b></td>\n");
					}
					if (rsclose.getString("status_name").equals("Closed Others")) {

						Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedothers=yes");
						Str.append(multiSelect + "><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")))
								.append("</b></a></td>");

						// Str.append("<td align=right><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))).append("</b></td>\n");
					}
				}
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");
				// Str.append("</div>\n");

			} else {
				Str.append("<font color=red><b>No Enquiry found!</b></font>");
			}
			rsstage.close();
			rsclose.close();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String EnquiryClosedSummary() {
		StringBuilder Str = new StringBuilder();
		String StrStage = "";
		String StrClose = "";
		HashMap<String, Integer> stagename = new LinkedHashMap<String, Integer>();
		HashMap<String, Integer> statusname = new LinkedHashMap<String, Integer>();

		StringBuilder multiSelect = new StringBuilder();
		multiSelect.append("&starttime=").append(starttime)
				.append("&endtime=").append(endtime)
				.append("&brand_id=").append(brand_id)
				.append("&region_id=").append(region_id)
				.append("&branch_id=").append(branch_id)
				.append("&model_id=").append(model_id)
				.append("&team_id=").append(team_id)
				.append("&exe_id=").append(exe_id)
				.append("&closedsummary=yes")
				.append(" target=_blank");

		// int totalbudget = 0;
		// NumberFormat nf = NumberFormat.getInstance();
		try {
			StrSql = "SELECT stage_id, stage_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage"
					+ " ORDER BY stage_rank";
			ResultSet rsstage = processQuery(StrSql, 0);
			while (rsstage.next()) {
				StrStage += " COALESCE (SUM(IF(stage_name = '" + rsstage.getString("stage_name")
						+ "', 1, 0)), 0) AS '" + rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id") + "', ";

				// StrStage = StrStage + " " + rsstage.getString("stage_name") + ", ";
				stagename.put((rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id")), 0);
			}

			StrSql = "SELECT status_id, status_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status"
					+ " WHERE status_id > 2"
					+ " ORDER BY status_id";
			ResultSet rsclose = processQuery(StrSql, 0);
			while (rsclose.next()) {
				StrClose += " COALESCE (SUM(IF(status_name = '" + rsclose.getString("status_name")
						+ "', 1, 0)), 0) AS '" + rsclose.getString("status_name") + ":" + rsclose.getString("status_id") + "', ";

				// StrClose = StrClose + " " + rsclose.getString("status_name") + ", ";
				statusname.put((rsclose.getString("status_name") + ":" + rsclose.getString("status_id")), 0);
			}
			StrClose = StrClose.substring(0, StrClose.length() - 2);
			StrSql = " SELECT sob_id, sob_name AS SOB, " + StrStage
					+ " COUNT(DISTINCT enquiry_id) AS Total, " + StrClose
					+ " FROM " + compdb(comp_id) + "axela_sob "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_sob_id=sob_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id=enquiry_stage_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id=enquiry_status_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id "
					+ " WHERE 1 = 1"
					+ " AND enquiry_status_id > 2 "
					+ EnquirySearch + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY Total, SOB";
			// SOP("StrSql-------2-------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				RowSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				Str.append("<div class=\" \">\n");
				Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				for (int i = 2; i <= numberOfColumns; i++) {
					Str.append("<th data-toggle=\"true\">").append(rsmd.getColumnLabel(i).split(":")[0]).append("</th>");
				}
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				int rowcount = crs.getRow();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					String sob_id = crs.getString("sob_id");
					Str.append("<tr>");
					count++;

					rsstage.beforeFirst();
					while (rsstage.next()) {
						stagename.put(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"), stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id") + "")
								+ crs.getInt(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id")));
					}

					rsclose.beforeFirst();
					while (rsclose.next()) {
						statusname.put(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"),
								statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id") + "")
										+ crs.getInt(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")));
					}

					for (int i = 2; i <= numberOfColumns; i++) {
						if (i == 2) {
							Str.append("<td><b>").append(crs.getString(i)).append("</b></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Total")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&total=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + "><b>").append(crs.getString(i)).append("</b></a></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Closed Lost")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedlost=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");
						} else if (rsmd.getColumnName(i).split(":")[0].equals("Closed Others")) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedothers=yes");
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");
						}
						// SOP("NOOFCOLUMNS"+Str.toString());
						else if (count == rowcount) {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsmd.getColumnName(i).split(":")[1]);
							Str.append("&sob_id=" + sob_id + "&status_id=" + i).append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");

						} else {
							Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsmd.getColumnName(i).split(":")[1]);
							Str.append("&sob_id=" + sob_id + "&").append(multiSelect + ">").append(crs.getString(i)).append("</a></td>");

						}
					}
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");

				int totalstage = 0;

				rsstage.beforeFirst();
				while (rsstage.next()) {
					Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsstage.getString("stage_id")).append(multiSelect + "><b>")
							.append(stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"))).append("</b></a></td>\n");

					totalstage += stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"));
				}

				Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes").append(multiSelect + "><b>")
						.append(totalstage).append("</b></a></td>\n");

				rsclose.beforeFirst();
				while (rsclose.next()) {
					/*
					 * if (rsclose.getString("status_name").equals("Open")) {
					 * 
					 * // Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&stage_id=" + rsstage.getString("stage_id")).append(multiSelect + "><b>") //
					 * .append(stagename.get(rsstage.getString("stage_name") + ":" + rsstage.getString("stage_id"))).append("</b></a></td>\n");
					 * 
					 * Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&open=yes"); Str.append(multiSelect +
					 * "><b><font color=blue>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))) .append("<font></b></a></td>");
					 * 
					 * // Str.append("<td align=right><b><font color=blue>").append(statusname.get(rsclose.getString("status_name") + ":" + //
					 * rsclose.getString("status_id"))).append("</font></b></td>\n"); } if (rsclose.getString("status_name").equals("Closed Won")) {
					 * 
					 * Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedwon=yes"); Str.append(multiSelect +
					 * "><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))) .append("</b></a></td>");
					 * 
					 * // Str.append("<td align=right><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))).append("</b></td>\n"); }
					 */
					if (rsclose.getString("status_name").equals("Closed Lost")) {

						Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedlost=yes");
						Str.append(multiSelect + "><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")))
								.append("</b></a></td>");

						// Str.append("<td align=right><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))).append("</b></td>\n");
					}
					if (rsclose.getString("status_name").equals("Closed Others")) {

						Str.append("<td align=right><a href=").append(SearchURL).append("filter=yes&closedothers=yes");
						Str.append(multiSelect + "><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id")))
								.append("</b></a></td>");

						// Str.append("<td align=right><b>").append(statusname.get(rsclose.getString("status_name") + ":" + rsclose.getString("status_id"))).append("</b></td>\n");
					}
				}
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");

			} else {
				Str.append("<font color=red><b>No Enquiry found!</b></font>");
			}
			rsstage.close();
			rsclose.close();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ConversionSummary() {
		int count = 0;
		String StrSql = "";
		int enquirytotal = 0, tdtotal = 0, bookingtotal = 0, deliveriestotal = 0, enquirygrandtotal = 0;
		StringBuilder Str = new StringBuilder();

		StringBuilder multiSelect = new StringBuilder();
		multiSelect.append("&starttime=").append(starttime)
				.append("&endtime=").append(endtime)
				.append("&brand_id=").append(brand_id)
				.append("&region_id=").append(region_id)
				.append("&branch_id=").append(branch_id)
				.append("&model_id=").append(model_id)
				.append("&team_id=").append(team_id)
				.append("&exe_id=").append(exe_id)
				.append(" target=_blank");

		try {
			StrSql = "SELECT sob_id, sob_name, "
					+ " COALESCE (enquiry, 0) AS enquiry, "
					+ " COALESCE (testdrives, 0) AS testdrives, "
					+ " COALESCE (booking, 0) AS booking, "
					+ " COALESCE (deliveries, 0) AS deliveries "
					+ " FROM " + compdb(comp_id) + "axela_sob "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ " COUNT(DISTINCT enquiry_id) AS enquiry, "
					+ " enquiry_sob_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id "
					+ " WHERE 1 = 1" + EnquirySearch
					+ " GROUP BY enquiry_sob_id ) AS tblenquiry ON tblenquiry.enquiry_sob_id = sob_id "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ " COUNT(DISTINCT CASE	WHEN testdrive_fb_taken = 1 THEN testdrive_enquiry_id	END) AS testdrives, "
					+ " enquiry_sob_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id "
					+ " WHERE 1 = 1" + EnquirySearch
					+ " GROUP BY enquiry_sob_id ) AS tbltestdrive ON tbltestdrive.enquiry_sob_id = sob_id "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ "	SUM(IF (so_delivered_date = '', 1, 0)) AS booking, "
					+ " SUM(IF (so_delivered_date != '', 1, 0)) AS deliveries, "
					+ " enquiry_sob_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id "
					+ " WHERE 1 =1 "
					+ " AND so_active = 1" + EnquirySearch
					+ " GROUP BY enquiry_sob_id ) AS tblso ON tblso.enquiry_sob_id = sob_id "
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name ";

			// SOP("StrSql-------3-------------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<b>Conversion Summary</b>");
				Str.append("<div class=\" \">\n");
				Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">SOB</th>\n");
				Str.append("<th colspan=2>Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\" colspan=2>TD</th>\n");
				Str.append("<th data-hide=\"phone\" colspan=2>Booking</th>\n");
				Str.append("<th data-hide=\"phone\" colspan=2>Delivery</th>\n");
				Str.append("</tr>");
				Str.append("<tr align=center>\n");
				Str.append("<th></th>\n");
				Str.append("<th></th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					enquirygrandtotal += crs.getInt("enquiry");
				}

				crs.beforeFirst();
				while (crs.next()) {
					count++;
					enquirytotal += crs.getInt("enquiry");
					tdtotal += crs.getInt("testdrives");
					bookingtotal += crs.getInt("booking");
					deliveriestotal += crs.getInt("deliveries");
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><b>").append(crs.getString("sob_name")).append("</b></td>");

					Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=enquiry");
					Str.append("&sob_id=" + crs.getString("sob_id") + "&").append(multiSelect + ">").append(crs.getString("enquiry")).append("</a></td>");
					Str.append("<td align=right>").append(getPercentage((double) crs.getInt("enquiry"), (double) enquirygrandtotal)).append("</td>");

					Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=testdrive");
					Str.append("&sob_id=" + crs.getString("sob_id") + "&").append(multiSelect + ">").append(crs.getString("testdrives")).append("</a></td>");
					Str.append("<td align=right>").append(getPercentage((double) crs.getInt("testdrives"), (double) crs.getInt("enquiry"))).append("</td>");

					Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=booking");
					Str.append("&sob_id=" + crs.getString("sob_id") + "&").append(multiSelect + ">").append(crs.getString("booking")).append("</a></td>");
					Str.append("<td align=right>").append(getPercentage((double) crs.getInt("booking"), (double) crs.getInt("enquiry"))).append("</td>");

					Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=delivered");
					Str.append("&sob_id=" + crs.getString("sob_id") + "&").append(multiSelect + ">").append(crs.getString("deliveries")).append("</a></td>");
					Str.append("<td align=right>").append(getPercentage((double) crs.getInt("deliveries"), (double) crs.getInt("enquiry"))).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>&nbsp;</td>");
				Str.append("<td align=right><b>Total:</b></td>");

				Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=enquiry&");
				Str.append(multiSelect + "><b>").append(enquirytotal).append("</b></a></td>");
				Str.append("<td align=right><b>").append(getPercentage((double) enquirytotal, (double) enquirygrandtotal)).append("</b></td>");

				Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=testdrive&");
				Str.append(multiSelect + "><b>").append(tdtotal).append("</b></a></td>");
				Str.append("<td align=right><b>").append(getPercentage((double) tdtotal, (double) enquirytotal)).append("</b></td>");

				Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=booking&");
				Str.append(multiSelect + "><b>").append(bookingtotal).append("</b></a></td>");
				Str.append("<td align=right><b>").append(getPercentage((double) bookingtotal, (double) enquirytotal)).append("</b></td>");

				Str.append("<td align=right><a href=").append(SearchURL).append("conversion=yes&name=delivered&");
				Str.append(multiSelect + "><b>").append(deliveriestotal).append("</b></a></td>");
				Str.append("<td align=right><b>").append(getPercentage((double) deliveriestotal, (double) enquirytotal)).append("</b></td>");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PreparePieChart() {
		StrSql = " SELECT sob_id, sob_name, COUNT(DISTINCT enquiry_id) AS Total ";
		String CountSql = " SELECT Count(DISTINCT sob_id)";
		String StrJoin = " FROM " + compdb(comp_id) + "axela_sob "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_sob_id = sob_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id "
				+ " WHERE 1 = 1 "
				+ EnquirySearch + "";
		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin
				+ " GROUP BY sob_id"
				+ " ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		// SOP("gsh" + StrSql);
		// SOP("gsh" + CountSql);
		// SOP("too" + TotalRecords);
		int count = 0;
		// SOP("StrSql-------4-------------" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				chart_data = "[";
				while (crs.next()) {
					count++;
					// chart_data = chart_data + "['" + crs.getString("sob_name")
					// + " (" + crs.getString("Total") + ")'," +
					// crs.getString("Total") + "]";
					chart_data = chart_data + "{'type': '" + crs.getString("sob_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total = chart_data_total + crs.getInt("Total");
					// SOP("chart_data_total----------" + chart_data_total);
					if (count < TotalRecords) {
						chart_data = chart_data + ",";
					} else {
					}
				}
				chart_data = chart_data + "]";
				// SOP("chart_data----------" + chart_data);
			} else {
				NoChart = "No Enquiry Found!";
				// SOP("NoChart----------" + NoChart);
			}
			crs.close();
		} catch (SQLException ex) {
			Logger.getLogger(Report_SOB_Dash.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	private void SoeDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			String starttime = CNumeric(PadQuotes(request.getParameter("starttime")));
			String endtime = CNumeric(PadQuotes(request.getParameter("endtime")));
			String open = PadQuotes(request.getParameter("open"));
			String closedwon = PadQuotes(request.getParameter("closedwon"));
			String closedlost = PadQuotes(request.getParameter("closedlost"));
			String closedothers = PadQuotes(request.getParameter("closedothers"));
			String closedsummary = PadQuotes(request.getParameter("closedsummary"));
			String total = PadQuotes(request.getParameter("total"));
			String stage_id = PadQuotes(request.getParameter("stage_id"));
			String brand_id = PadQuotes(RetrunSelectArrVal(request, "brand_id"));
			String region_id = PadQuotes(RetrunSelectArrVal(request, "region_id"));
			String branch_id = PadQuotes(RetrunSelectArrVal(request, "branch_id"));
			String model_id = PadQuotes(RetrunSelectArrVal(request, "model_id"));
			String team_id = PadQuotes(RetrunSelectArrVal(request, "team_id"));
			String exe_id = PadQuotes(request.getParameter("exe_id"));
			String sob_id = PadQuotes(request.getParameter("sob_id"));
			String sob_ids = PadQuotes(RetrunSelectArrVal(request, "sob_ids"));
			String name = PadQuotes(request.getParameter("name"));
			if (!sob_ids.equals("")) {
				sob_ids = CleanArrVal(sob_ids);
			}

			// SOP("starttime===" + starttime);
			// SOP("endtime===" + endtime);
			// SOP("open===" + open);
			// SOP("closedwon===" + closedwon);
			// SOP("closedlost===" + closedlost);
			// SOP("closedothers===" + closedothers);
			// SOP("brand_id===" + brand_id);
			// SOP("region_id===" + region_id);
			// SOP("branch_id===" + branch_id);
			// SOP("model_id===" + model_id);
			// SOP("team_id===" + team_id);
			// SOP("exe_id===" + exe_id);
			// SOP("model_id===" + model_id);
			// SOP("stage_id===" + stage_id);
			// SOP("sob_id===" + sob_id);
			// SOP("sob_ids===" + sob_ids);

			// Brand
			if (!brand_id.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			// Regions
			if (!region_id.equals("")) {
				StrSearch += " AND branch_region_id IN (" + region_id + ")";
			}
			// Branch
			if (!branch_id.equals("")) {
				StrSearch += " AND branch_id IN (" + branch_id + ")";
			}
			// Models
			if (!model_id.equals("")) {
				StrSearch += " AND model_id IN (" + model_id + ")";
			}

			if (!team_id.equals("")) {
				StrSearch += " AND enquiry_emp_id IN (SELECT teamtrans_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id IN (" + team_id + "))";
			}

			if (!exe_id.equals("")) {
				StrSearch += " AND enquiry_emp_id IN (" + exe_id + ")";
			}

			StrSearch += " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			if (!sob_id.equals("")) {
				StrSearch += " AND enquiry_sob_id = " + sob_id;
			}

			if (!sob_ids.equals("")) {
				StrSearch += " AND enquiry_sob_id IN (" + sob_ids + ")";
			}
			if (open.equals("yes")) {
				StrSearch += " AND enquiry_status_id = 1";
			}
			if (closedwon.equals("yes")) {
				StrSearch += " AND enquiry_status_id = 2";
			}
			if (closedlost.equals("yes")) {
				StrSearch += " AND enquiry_status_id = 3";
			}
			if (closedothers.equals("yes")) {
				StrSearch += " AND enquiry_status_id = 4";
			}
			if (closedsummary.equals("yes")) {
				StrSearch += " AND enquiry_status_id > 2";
			}

			if (!stage_id.equals("")) {
				StrSearch += " AND enquiry_stage_id = " + stage_id;
			}

			// StrFilter = " AND enquiry_id IN ("
			// + " SELECT "
			// + " DISTINCT enquiry_id "
			// + " FROM	" + compdb(comp_id) + "axela_service_followup"
			// + " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id "
			// + " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
			// + " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
			// + " WHERE 1 = 1 "
			// + StrSearch
			// + " GROUP BY enquiry_id"
			// + ")";
			// SOP("StrFilter==" + StrFilter);
			SetSession("enquirystrsql", StrSearch, request);
			response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void ConversionDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			String starttime = CNumeric(PadQuotes(request.getParameter("starttime")));
			String endtime = CNumeric(PadQuotes(request.getParameter("endtime")));
			String brand_id = PadQuotes(RetrunSelectArrVal(request, "brand_id"));
			String region_id = PadQuotes(RetrunSelectArrVal(request, "region_id"));
			String branch_id = PadQuotes(RetrunSelectArrVal(request, "branch_id"));
			String model_id = PadQuotes(RetrunSelectArrVal(request, "model_id"));
			String team_id = PadQuotes(RetrunSelectArrVal(request, "team_id"));
			String exe_id = PadQuotes(request.getParameter("exe_id"));
			String sob_id = PadQuotes(request.getParameter("sob_id"));
			String name = PadQuotes(request.getParameter("name"));

			// Brand
			if (!brand_id.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			// Regions
			if (!region_id.equals("")) {
				StrSearch += " AND branch_region_id IN (" + region_id + ")";
			}
			// Branch
			if (!branch_id.equals("")) {
				StrSearch += " AND branch_id IN (" + branch_id + ")";
			}
			// Models
			if (!model_id.equals("")) {
				StrSearch += " AND model_id IN (" + model_id + ")";
			}

			if (!team_id.equals("")) {
				StrSearch += " AND enquiry_emp_id IN (SELECT teamtrans_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id IN (" + team_id + "))";
			}

			if (!exe_id.equals("")) {
				StrSearch += " AND enquiry_emp_id IN (" + exe_id + ")";
			}

			StrSearch += " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			if (!sob_id.equals("")) {
				StrSearch += " AND enquiry_sob_id = " + sob_id;
			}

			if (name.equals("enquiry")) {

				StrFilter = " AND enquiry_id IN ("
						+ " SELECT"
						+ " enquiry_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE 1 = 1"
						+ StrSearch
						+ ")";
				// SOP("StrFilter==" + StrFilter);
				SetSession("enquirystrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
			}
			if (name.equals("testdrive")) {
				StrFilter = " AND testdrive_id IN ("
						+ " SELECT"
						+ " testdrive_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id"
						+ " WHERE 1 = 1"
						+ " AND testdrive_fb_taken = 1"
						+ StrSearch
						+ " GROUP BY enquiry_id "
						+ ")";
				// SOP("StrFilter==" + StrFilter);
				SetSession("testdrivestrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/testdrive-list.jsp?smart=yes"));
			}
			if (name.equals("booking")) {
				StrFilter = " AND so_enquiry_id IN ("
						+ " SELECT"
						+ " enquiry_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
						+ " WHERE 1 = 1"
						+ " AND so_delivered_date = ''"
						+ " AND so_active = 1"
						+ StrSearch
						+ ")";

				// SOP("StrFilter==" + StrFilter);

				SetSession("sostrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
			}
			if (name.equals("delivered")) {
				StrFilter = " AND so_enquiry_id IN ("
						+ " SELECT"
						+ " enquiry_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
						+ " WHERE 1 = 1"
						+ " AND so_delivered_date != ''"
						+ " AND so_active = 1"
						+ StrSearch
						+ ")";
				// SOP("StrFilter==" + StrFilter);
				SetSession("sostrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
