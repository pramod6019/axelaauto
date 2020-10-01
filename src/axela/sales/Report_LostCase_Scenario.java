package axela.sales;
//Manjur
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Demo;

public class Report_LostCase_Scenario extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "";
	public String comp_id = "0";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "", brand_id = "", region_id = "";
	public String StrHTML = "", StrHTML1 = "", StrHTML2 = "", StrClosedHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public int chart_data_total1 = 0;
	public int chart_data_total2 = 0;
	public int chart_data_total3 = 0;
	public String go = "";
	public String NoChart = "";
	public String StrSql = "";
	public String lostcase1_id = "0";
	public String lostcase1_name = "";
	public String lostcase2_id = "0";
	public String lostcase2_name = "";
	public String lostcase2 = "";
	public String lostcase3 = "";
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
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

					StrSearch = BranchAccess + ExeAccess;

					if (!starttime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(enquiry_status_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(enquiry_status_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND enquiry_branch_id IN(" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						// StrSearch = StrSearch + " AND team_id in (" + team_id
						// + ")";
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND enquiry_emp_id IN (SELECT teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrClosedHTML = SOESummary();
						StrHTML = LostCase1Summary() + lostcase2 + lostcase3;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if (dr_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
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

	public String SOESummary() {
		int total = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_name, count(enquiry_id) AS Total,"
					+ " (SELECT count(grand_enquiry.enquiry_id) AS Total"
					+ " FROM " + compdb(comp_id) + "axela_soe grand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry grand_enquiry ON grand_enquiry.enquiry_soe_id = grand.soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp grand_emp ON grand_emp.emp_id = grand_enquiry.enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch grand_branch ON grand_branch.branch_id = grand_enquiry.enquiry_branch_id"
					+ " WHERE grand_enquiry.enquiry_status_id > 2"
					+ (StrSearch.replace("enquiry", "grand_enquiry.enquiry"))
					+ " ORDER BY grand_enquiry.enquiry_status_date) AS grand";
			String StrJoin = " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_soe_id = soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " WHERE 1 = 1"
					+ " AND enquiry_status_id>2 "
					+ StrSearch + "";
			StrJoin = StrJoin + " GROUP BY soe_id "
					+ " ORDER BY Total DESC";
			StrSql = StrSql + StrJoin;

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"form-element12\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>SOE</th>");
				Str.append("<th width=\"10%\">%</th>");
				Str.append("<th width=\"10%\">Total</th>");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				while (crs.next()) {
					total = total + crs.getInt("total");
					Str.append("<tr>");
					Str.append("<td>").append(crs.getString("soe_name")).append("</td>");
					Str.append("<td align=right>").append(getPercentage(crs.getInt("total"), crs.getInt("grand"))).append("</td>");
					Str.append("<td align=right>").append(crs.getString("total")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right>&nbsp;</td>");
				Str.append("<td align=right><b>Total: </b></td>");
				Str.append("<td align=right><b>").append(total).append("</b></td>");
				Str.append("</tr>");
				Str.append("</tbody>");
				Str.append("</table>");
				Str.append("</div>");
				crs.beforeFirst();
				chart_data = "[";
				while (crs.next()) {
					chart_data = chart_data + "{'type': '" + crs.getString("soe_name") + "', 'total':" + crs.getString("Total") + "},";
					chart_data_total = chart_data_total + crs.getInt("Total");
				}
				chart_data = chart_data.substring(0, chart_data.length() - 1) + "]";
			} else {
				NoChart = "No Lost Case SOE Summary Found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String LostCase1Summary() {
		int total = 0;
		int charttotal = 0;
		String chartdata = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT lostcase1_id, lostcase1_name, count(enquiry_id) AS total"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_lostcase1_id = lostcase1_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
					+ " WHERE enquiry_status_id>2 " + StrSearch + ""
					+ " GROUP BY lostcase1_name "
					+ " ORDER BY total DESC";
			// SOP(" StrSql in LostCase1Summary closed==========" +
			// StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// // Start Chart1
				chartdata = "[";
				while (crs.next()) {
					chartdata = chartdata + "{'type': '" + crs.getString("lostcase1_name") + "', 'total':" + crs.getString("total") + "},";
					charttotal += +crs.getInt("Total");
				}
				chartdata = chartdata.substring(0, chartdata.length() - 1) + "]";

				Str.append("<br><script type=\"text/javascript\">\n");
				Str.append("var chart1;\n");
				Str.append("var legend1;\n");
				Str.append("var export2 = {\n");
				Str.append("menuTop:\"0px\",\n");
				Str.append("menuItems: [{\n");
				Str.append("icon: '../Library/amcharts/images/export.png',\n");
				Str.append("format: 'jpg'\n");
				Str.append("}],\n");
				Str.append("menuItemOutput:{\n");
				Str.append("fileName: \"Lost Case Scenario\"\n");
				Str.append("}\n");
				Str.append("};\n");
				Str.append("AmCharts.ready(function () {\n");
				Str.append("var chartData1 = " + chartdata + ";\n");
				// PIE CHART
				Str.append("chart1 = new AmCharts.AmPieChart();\n");
				Str.append("chart1.dataProvider = chartData1;\n");
				Str.append("chart1.titleField = \"type\";\n");
				Str.append("chart1.valueField = \"total\";\n");
				Str.append("chart1.minRadius = 200;\n");
				// LEGEND
				Str.append("legend1 = new AmCharts.AmLegend();\n");
				Str.append("legend1.align = \"center\";\n");
				Str.append("legend1.markerType = \"circle\";\n");
				Str.append("chart1.balloonText = \"[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>\";\n");
				Str.append("chart1.exportConfig = export2;\n");
				Str.append("chart1.addLegend(legend1);\n");
				// WRITE
				Str.append("chart1.write(\"chart2\");\n");
				Str.append("});\n");
				Str.append("</script>\n");
				Str.append("<div class=\"form-element12\" id=\"chart2\" style=\"height:1000px;\"></div>\n");
				Str.append("<b>Total: " + charttotal + "</b>\n");

				crs.beforeFirst();
				// // End Chart1

				Str.append("<div class=\"form-element12\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>Lost Case</th>\n");
				Str.append("<th width=\"10%\">%</th>\n");
				Str.append("<th width=\"10%\">Total</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					lostcase1_id = crs.getString("lostcase1_id");
					lostcase1_name = crs.getString("lostcase1_name");
					total = total + crs.getInt("total");
					Str.append("<tr>\n");
					Str.append("<td>").append(crs.getString("lostcase1_name")).append("</td>\n");
					Str.append("<td align=right>").append(getPercentage(crs.getInt("total"), charttotal)).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("total")).append("</td>\n");
					Str.append("</tr>\n");
					lostcase2 += LostCase2Summary(crs.getString("lostcase1_id"), crs.getString("lostcase1_name"));
				}

				Str.append("<tr>\n");
				Str.append("<td align=right>&nbsp;</td>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(total).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");

			} else {
				Str.append("No Lost Case 1 Found!");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String LostCase2Summary(String lostcase1_id, String lostcase1_name) {
		int total = 0;
		int charttotal = 0;
		String chartdata = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT lostcase2_id, lostcase2_name, count(enquiry_id) AS total"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_lostcase2_id = lostcase2_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
					+ " WHERE enquiry_status_id>2"
					+ " AND lostcase2_lostcase1_id = " + lostcase1_id
					+ StrSearch + ""
					+ " GROUP BY lostcase2_name "
					+ " ORDER BY total DESC";
			// SOP("StrSql in LostCase2Summary closed==========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// // Start Chart
				chartdata = "[";
				while (crs.next()) {
					chartdata = chartdata + "{'type': '" + crs.getString("lostcase2_name") + "', 'total':" + crs.getString("total") + "},";
					charttotal += +crs.getInt("Total");
				}
				chartdata = chartdata.substring(0, chartdata.length() - 1) + "]";

				Str.append("<br><script type=\"text/javascript\">\n");
				Str.append("var chart2" + lostcase1_id + ";\n");
				Str.append("var legend2" + lostcase1_id + ";\n");
				Str.append("var export2" + lostcase1_id + " = {\n");
				Str.append("menuTop:\"0px\",\n");
				Str.append("menuItems: [{\n");
				Str.append("icon: '../Library/amcharts/images/export.png',\n");
				Str.append("format: 'jpg'\n");
				Str.append("}],\n");
				Str.append("menuItemOutput:{\n");
				Str.append("fileName: \"Lost Case Scenario\"\n");
				Str.append("}\n");
				Str.append("};\n");
				Str.append("AmCharts.ready(function () {\n");
				// PIE CHART
				Str.append("chart2" + lostcase1_id + " = new AmCharts.AmPieChart();\n");
				Str.append("chart2" + lostcase1_id + ".dataProvider = " + chartdata + ";\n");
				Str.append("chart2" + lostcase1_id + ".titleField = \"type\";\n");
				Str.append("chart2" + lostcase1_id + ".valueField = \"total\";\n");
				Str.append("chart2" + lostcase1_id + ".minRadius = 200;\n");
				// LEGEND
				Str.append("legend2" + lostcase1_id + " = new AmCharts.AmLegend();\n");
				Str.append("legend2" + lostcase1_id + ".align = \"center\";\n");
				Str.append("legend2" + lostcase1_id + ".markerType = \"circle\";\n");
				Str.append("chart2" + lostcase1_id + ".balloonText = \"[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>\";\n");
				Str.append("chart2" + lostcase1_id + ".exportConfig = export2" + lostcase1_id + ";\n");
				Str.append("chart2" + lostcase1_id + ".addLegend(legend2" + lostcase1_id + ");\n");
				// WRITE
				Str.append("chart2" + lostcase1_id + ".write(\"chart2" + lostcase1_id + "\");\n");
				Str.append("});\n");
				Str.append("</script>\n");
				Str.append("<div class=\"form-element12\" id=\"chart2" + lostcase1_id + "\" style=\"height:1000px;\"></div>\n");
				Str.append("<b>Total: " + charttotal + "</b>\n");

				crs.beforeFirst();
				// // End Chart

				Str.append("<br>");

				Str.append("<div class=\"form-element12\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>" + lostcase1_name + "</th>\n");
				Str.append("<th width=\"10%\">%</th>\n");
				Str.append("<th width=\"10%\">Total</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("</tbody>\n");
				while (crs.next()) {
					lostcase2_id = crs.getString("lostcase2_id");
					lostcase2_name = crs.getString("lostcase2_name");
					total = total + crs.getInt("total");
					Str.append("<tr>\n");
					Str.append("<td>").append(crs.getString("lostcase2_name")).append("</td>\n");
					Str.append("<td align=right>").append(getPercentage(crs.getInt("total"), charttotal)).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("total")).append("</td>\n");
					Str.append("</tr>\n");
					lostcase3 += LostCase3Summary(crs.getString("lostcase2_id"), lostcase1_name, crs.getString("lostcase2_name"));

				}
				Str.append("<tr>\n");
				Str.append("<td align=right>&nbsp;</td>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(total).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String LostCase3Summary(String lostcase2_id, String lostcase1_name, String lostcase2_name) {
		int total = 0;
		int charttotal = 0;
		String chartdata = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT lostcase3_id, lostcase3_name, count(enquiry_id) AS total"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_lostcase3_id = lostcase3_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
					+ " WHERE enquiry_status_id > 2"
					+ " AND lostcase3_lostcase2_id = " + lostcase2_id
					+ StrSearch + ""
					+ " GROUP BY lostcase3_name "
					+ " ORDER BY total DESC";
			// SOP("StrSql in LostCase2Summary closed==========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// // Start Chart
				chartdata = "[";
				while (crs.next()) {
					chartdata = chartdata + "{'type': '" + crs.getString("lostcase3_name") + "', 'total':" + crs.getString("total") + "},";
					charttotal += +crs.getInt("Total");
				}
				chartdata = chartdata.substring(0, chartdata.length() - 1) + "]";

				Str.append("<br><script type=\"text/javascript\">\n");
				Str.append("var chart3" + lostcase2_id + ";\n");
				Str.append("var legend3" + lostcase2_id + ";\n");
				Str.append("var export3" + lostcase2_id + " = {\n");
				Str.append("menuTop:\"0px\",\n");
				Str.append("menuItems: [{\n");
				Str.append("icon: '../Library/amcharts/images/export.png',\n");
				Str.append("format: 'jpg'\n");
				Str.append("}],\n");
				Str.append("menuItemOutput:{\n");
				Str.append("fileName: \"Lost Case Scenario\"\n");
				Str.append("}\n");
				Str.append("};\n");
				Str.append("AmCharts.ready(function () {\n");
				// PIE CHART
				Str.append("chart3" + lostcase2_id + " = new AmCharts.AmPieChart();\n");
				Str.append("chart3" + lostcase2_id + ".dataProvider = " + chartdata + ";\n");
				Str.append("chart3" + lostcase2_id + ".titleField = \"type\";\n");
				Str.append("chart3" + lostcase2_id + ".valueField = \"total\";\n");
				Str.append("chart3" + lostcase2_id + ".minRadius = 200;\n");
				// LEGEND
				Str.append("legend3" + lostcase2_id + " = new AmCharts.AmLegend();\n");
				Str.append("legend3" + lostcase2_id + ".align = \"center\";\n");
				Str.append("legend3" + lostcase2_id + ".markerType = \"circle\";\n");
				Str.append("chart3" + lostcase2_id + ".balloonText = \"[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>\";\n");
				Str.append("chart3" + lostcase2_id + ".exportConfig = export3" + lostcase2_id + ";\n");
				Str.append("chart3" + lostcase2_id + ".addLegend(legend3" + lostcase2_id + ");\n");
				// WRITE
				Str.append("chart3" + lostcase2_id + ".write(\"chart3" + lostcase2_id + "\");\n");
				Str.append("});\n");
				Str.append("</script>\n");
				Str.append("<div class=\"form-element12\" id=\"chart3" + lostcase2_id + "\" style=\"height:700px;\"></div>\n");
				Str.append("<b>Total: " + charttotal + "</b>\n");

				crs.beforeFirst();
				// // End Chart

				Str.append("<br>");

				Str.append("<div class=\"form-element12\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>" + lostcase1_name + " > " + lostcase2_name + "</th>\n");
				Str.append("<th width=\"10%\">%</th>\n");
				Str.append("<th width=\"10%\">Total</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					total = total + crs.getInt("total");
					Str.append("<tr>\n");
					Str.append("<td>").append(crs.getString("lostcase3_name")).append("</td>\n");
					Str.append("<td align=right>").append(getPercentage(crs.getInt("total"), charttotal)).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("total")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=right>&nbsp;</td>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(total).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateGroup() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT group_id, group_name"
					+ " FROM " + compdb(comp_id) + "axela_group"
					+ " ORDER BY group_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("group_id"));
				Str.append(ArrSelectdrop(crs.getInt("group_id"), team_ids));
				Str.append(">").append(crs.getString("group_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
