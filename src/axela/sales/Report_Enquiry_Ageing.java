package axela.sales;

// modified - 24, 26, 27,28 august 2013
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Enquiry_Ageing extends Connect {

	public String StrSql = "", StrTAT = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "0";
	// public String[] team_ids, exe_ids, model_ids, soe_ids, sob_ids;
	public String[] team_ids, exe_ids, model_ids, soe_ids, sob_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "", soe_id = "", sob_id = "", brand_id = "", region_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String ageingprioritycount = "", ageingdayscount = "";
	public String AgeingSearchStr = "", AgeingDaysSearchStr = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrSob = "";
	public String StrExe = "";
	public String StrTeam = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String SearchURL = "report-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String StrClosedHTML = "";
	public String StrClosedHTML1 = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String NoChart = "";
	public String chart_data1 = "";
	public int chart_data_total1 = 0;
	public String NoChart1 = "";
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm("comp_id", "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				if (!header.equals("no")) {
					CheckSession(request, response);
					emp_id = CNumeric(GetSession("emp_id", request));
					// branch_id =
					// (session.getAttribute("emp_branch_id")).toString();
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
					ageingprioritycount = PadQuotes(request.getParameter("ageingprioritycount"));
				}
				if (!header.equals("no")) {
					GetValues(request, response);
					// CheckForm();
					if (go.equals("Go")) {
						StrSearch = BranchAccess.replace("branch_id", "emp_branch_id");

						StrSearch += ExeAccess.replace("emp_id", "enquiry_emp_id");

						if (!brand_id.equals("")) {
							StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id IN (" + region_id + ") ";
						}
						if (!branch_id.equals("")) {
							mischeck.exe_branch_id = branch_id;
							StrSearch = StrSearch + " and enquiry_branch_id in (" + branch_id + ")";
						}
						if (!exe_id.equals("")) {
							StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
						}
						if (!team_id.equals("")) {
							mischeck.exe_branch_id = branch_id;
							mischeck.branch_id = branch_id;
							StrSearch = StrSearch + " and teamtrans_team_id in (" + team_id + ")";
						}
						if (!model_id.equals("")) {
							StrSearch = StrSearch + " and enquiry_model_id in (" + model_id + ")";
						}
						if (!soe_id.equals("")) {
							StrSearch = StrSearch + " and enquiry_soe_id in (" + soe_id + ")";
						}
						if (!sob_id.equals("")) {
							StrSearch = StrSearch + " and enquiry_sob_id in (" + sob_id + ")";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							StrHTML = ListAgeing();
						}
					} else if (ageingprioritycount.equals("yes")) {
						AgeingSearchStr = PadQuotes(request.getParameter("AgeingSearchStr"));
						// SOP("AgeingSearchStr===" +
						// URLDecoder.decode(unescapehtml(AgeingSearchStr),
						// "UTF-8"));
						if (!AgeingSearchStr.equals("")) {
							StrSearch = " AND enquiry_id IN (SELECT enquiry_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE 1=1"
									+ " AND enquiry_status_id = 1"
									+ " " + URLDecoder.decode(unescapehtml(AgeingSearchStr), "UTF-8")
									+ " )";
						}

						SetSession("enquirystrsql", StrSearch, request);
						response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
					}
				} else {
					dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
					starttime = PadQuotes(request.getParameter("txt_starttime"));
					endtime = PadQuotes(request.getParameter("txt_endtime"));
					targetstarttime = starttime;
					targetendtime = endtime;
					chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
					StrHTML = ListAgeing();
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
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
		// SOP("brand_id-----------" + brand_id);
		// SOP("brand_ids-----------" + brand_ids);
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
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
				targetstarttime = starttime.substring(0, 8) + "000000";
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("")
						&& Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				targetendtime = endtime.substring(0, 8) + "000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListAgeing() {
		int total_0days = 0;
		int total_1days = 0;
		int total_2days = 0;
		int total_3days = 0;
		int total_4days = 0;
		int total_5days = 0;
		int total_6days = 0;
		int total_7days = 0;
		int total_8days = 0;
		int total_9days = 0;
		int total_10days = 0;
		int total_11_15days = 0;
		int total_16_20days = 0;
		int total_21_25days = 0;
		int total_26_30days = 0;
		int total_gt30days = 0;
		int total_30_45days = 0;
		int total_46_60days = 0;
		int total_gt60days = 0;

		int total_total = 0;
		int total_avg = 0;
		int total = 0;
		String average = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT priorityenquiry_name, enquiry_priorityenquiry_id,"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 0, 1, 0)) as '0Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 1, 1, 0)) as '1Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 2, 1, 0)) as '2Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 3, 1, 0)) as '3Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 4, 1, 0)) as '4Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 5, 1, 0)) as '5Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 6, 1, 0)) as '6Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 7, 1, 0)) as '7Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 8, 1, 0)) as '8Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 9, 1, 0)) as '9Days',"
					+ " SUM(IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 10, 1, 0)) as '10Days',"
					+ " SUM(IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 11 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=15), 1, 0)) as '11-15Days',"
					+ " SUM(IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 16 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=20), 1, 0)) as '16-20Days',"
					+ " SUM(IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 21 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=25), 1, 0)) as '21-25Days',"
					+ " SUM(IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 26 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=30), 1, 0)) as '26-30Days',"
					+ " SUM(IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 30 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=45), 1, 0)) as '31-45Days',"
					+ " SUM(IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 46 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=60), 1, 0)) as '46-60Days',"
					+ " SUM(IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 61), 1, 0)) as '>60Days'"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_priorityenquiry_id = priorityenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_sales='1'"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR(" + ToLongDate(kknow()) + ", 1, 8)"
					+ " AND enquiry_status_id = 1 "
					+ StrSearch + ""
					+ " GROUP BY priorityenquiry_id"
					+ " ORDER BY priorityenquiry_name";
			// SOP("StrSql-----Ageing-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
			if (crs.isBeforeFirst()) {
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Priority</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">0 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">1 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">2 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">3 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">4 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">5 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">6 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">7 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">8 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">9 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">10 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">11-15 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">16-20 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">21-25 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">26-30 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">31-45 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">46-60 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">>60 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Total</th>\n");
				// Str.append("<th>Avg Days</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				while (crs.next()) {

					total_0days += crs.getInt("0Days");
					total_1days += crs.getInt("1Days");
					total_2days += crs.getInt("2Days");
					total_3days += crs.getInt("3Days");
					total_4days += crs.getInt("4Days");
					total_5days += crs.getInt("5Days");
					total_6days += crs.getInt("6Days");
					total_7days += crs.getInt("7Days");
					total_8days += crs.getInt("8Days");
					total_9days += crs.getInt("9Days");
					total_10days += crs.getInt("10Days");
					total_11_15days += crs.getInt("11-15Days");
					total_16_20days += crs.getInt("16-20Days");
					total_21_25days += crs.getInt("21-25Days");
					total_26_30days += crs.getInt("26-30Days");
					total_30_45days += crs.getInt("31-45Days");
					total_46_60days += crs.getInt("46-60Days");
					total_gt60days += crs.getInt(">60Days");
					// total_gt30days += crs.getInt(">30Days");
					// total_avg += crs.getDouble("average");
					total = crs.getInt("0Days") + crs.getInt("1Days")
							+ crs.getInt("2Days") + crs.getInt("3Days")
							+ crs.getInt("4Days") + crs.getInt("5Days")
							+ crs.getInt("6Days") + crs.getInt("7Days")
							+ crs.getInt("8Days") + crs.getInt("9Days")
							+ crs.getInt("10Days") + crs.getInt("11-15Days")
							+ crs.getInt("16-20Days") + crs.getInt("21-25Days")
							+ crs.getInt("26-30Days") + crs.getInt("31-45Days")
							+ crs.getInt("46-60Days") + crs.getInt(">60Days");
					total_total += total;
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("priorityenquiry_name") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("0Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("1Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("2Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("3Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("4Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("5Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("6Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("7Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("8Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("9Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("10Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("11-15Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("16-20Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("21-25Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("26-30Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("31-45Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("46-60Days") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString(">60Days") + "</td>");
					Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
							.append(URLEncoder.encode(StrSearch + " AND enquiry_priorityenquiry_id=" + crs.getString("enquiry_priorityenquiry_id"), "UTF-8")).append("\">")
							.append(total).append("<b></a></td>\n");
					Str.append("</tr>\n");
					total = 0;
				}
				average = (total_avg / (double) count) + "";
				Str.append("<tr align=center>\n");
				Str.append("<td valign=top align=right colspan=2><b>Total :</b></td>");
				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 0, 1, 0))", "UTF-8")).append("\">")
						.append(total_0days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 1, 1, 0))", "UTF-8")).append("\">")
						.append(total_1days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 2, 1, 0))", "UTF-8")).append("\">")
						.append(total_2days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 3, 1, 0))", "UTF-8")).append("\">")
						.append(total_3days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 4, 1, 0))", "UTF-8")).append("\">")
						.append(total_4days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 5, 1, 0))", "UTF-8")).append("\">")
						.append(total_5days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 6, 1, 0))", "UTF-8")).append("\">")
						.append(total_6days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 7, 1, 0))", "UTF-8")).append("\">")
						.append(total_7days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 8, 1, 0))", "UTF-8")).append("\">")
						.append(total_8days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 9, 1, 0))", "UTF-8")).append("\">")
						.append(total_9days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder.encode(StrSearch + " AND (IF(DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) = 10, 1, 0))", "UTF-8")).append("\">")
						.append(total_10days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 11 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=15), 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_11_15days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 16 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=20), 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_16_20days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 21 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=25), 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_21_25days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 26 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=30), 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_26_30days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 31 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=45), 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_30_45days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 46 && DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) <=60), 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_46_60days).append("</b></a></td>\n");

				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date) >= 61), 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_gt60days).append("<b></a></td>\n");
				Str.append("<td align=right><b><a href=\"../sales/report-enquiry-ageing.jsp?ageingprioritycount=yes&AgeingSearchStr=")
						.append(URLEncoder
								.encode(StrSearch + " AND IF((DATEDIFF(" + ToLongDate(kknow()) + ",enquiry_date)) = total, 1, 0)", "UTF-8"))
						.append("\">")
						.append(total_total).append("<b></a></td>\n");

				// Str.append("<td valign=top align=right><b>" + total_total +
				// "</b></td>");
				Str.append("</tr>\n");
			} else {
				Str.append("<br><br><br><br><b><font color=red>No Enquiry Ageing Found!</font></b><br><br>\n");
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");

			return Str.toString();
		} catch (Exception ex) {
			SOPError("SilverArrows== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select team_id, team_name " + " from " + compdb(comp_id) + "axela_sales_team "
					+ " where team_branch_id=" + dr_branch_id + " "
					+ " group by team_id " + " order by team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id"))
						.append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name"))
						.append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("SilverArrows== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1' and emp_sales='1' and "
					+ " (emp_branch_id = "
					+ dr_branch_id
					+ " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = "
					+ dr_branch_id
					+ ")) "
					+ ExeAccess + "";
			if (!team_id.equals("")) {
				StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			}
			StrSql = StrSql + " group by emp_id order by emp_name";
			// SOP("PopulateSalesExecutives..." + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"))
						.append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("SilverArrows== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateModel() {
		String stringval = "";
		try {
			StrSql = "select model_id, model_name from " + compdb(comp_id) + "axela_inventory_item_model "
					+ " inner join axela_inventory_item on item_model_id=model_id"
					+ " group by model_id order by model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				stringval = stringval + "<option value="
						+ crs.getString("model_id") + "";
				stringval = stringval
						+ ArrSelectdrop(crs.getInt("model_id"), model_ids);
				stringval = stringval + ">" + crs.getString("model_name")
						+ "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("SilverArrows== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return stringval;
	}

	public String PopulateSoe() {
		String sb = "";
		try {
			StrSql = " select soe_id, soe_name " + " from " + compdb(comp_id) + "axela_soe "
					// +
					// " inner join axela_inventory_item on item_model_id=model_id"
					+ " order by soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				sb = sb + "<option value=" + crs.getString("soe_id") + "";
				sb = sb + ArrSelectdrop(crs.getInt("soe_id"), soe_ids);
				sb = sb + ">" + crs.getString("soe_name") + "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("SilverArrows== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return sb;
	}

	public String PopulateSob() {
		String sb = "";
		try {
			StrSql = " select sob_id, sob_name " + " from " + compdb(comp_id) + "axela_sob "
					// +
					// " inner join axela_inventory_item on item_model_id=model_id"
					+ " order by sob_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				sb = sb + "<option value=" + crs.getString("sob_id") + "";
				sb = sb + ArrSelectdrop(crs.getInt("sob_id"), sob_ids);
				sb = sb + ">" + crs.getString("sob_name") + "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("SilverArrows== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return sb;
	}
}
