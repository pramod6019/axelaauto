package axela.sales;
// modified - 24, 26, 27,28 august 2013
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_TAT extends Connect {

	public String StrSql = "", StrTAT = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, soe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "", soe_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
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
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				if (!header.equals("no")) {
					CheckSession(request, response);
					emp_id = CNumeric(GetSession("emp_id", request));
					branch_id = CNumeric(GetSession("emp_branch_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
				}
				if (!header.equals("no")) {
					GetValues(request, response);
					CheckForm();
					if (go.equals("Go")) {
						StrSearch = BranchAccess.replace("branch_id", "team_branch_id");

						StrSearch += ExeAccess;

						if (!exe_id.equals("")) {
							StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
						}
						if (!brand_id.equals("")) {
							StrSearch += " AND team_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_brand_id IN (" + brand_id + ")) ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND team_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_region_id IN (" + region_id + ")) ";
						}
						if (!branch_id.equals("")) {
							mischeck.exe_branch_id = branch_id;
							StrBranch = " AND team_branch_id IN (" + branch_id + ")";
							// SOP("branch_id===" + branch_id);
						}
						// if (!branch_id.equals("")) {
						// StrSearch = StrSearch + " AND emp_branch_id IN(" +
						// branch_id +
						// ") OR emp_id= 1 OR emp_id IN (select empbr.emp_id from "
						// + " " + compdb(comp_id) +
						// "axela_emp_branch empbr WHERE " + compdb(comp_id) +
						// "axela_emp.emp_id=empbr.emp_id AND "
						// + " empbr.emp_branch_id IN (" + branch_id + "))";
						//
						// StrBranch = " AND branch_id IN(" + branch_id + ")";
						// }
						if (!team_id.equals("")) {
							mischeck.exe_branch_id = branch_id;
							mischeck.branch_id = branch_id;
							StrSearch = StrSearch + " AND team_id IN (" + team_id + ") AND team_branch_id IN (" + branch_id + ")";
							StrTeam = " AND team_id IN (" + team_id + ")  AND team_branch_id IN (" + branch_id + ")";
						}
						if (!model_id.equals("")) {
							StrModel = " AND item_model_id IN (" + model_id + ")";
						}
						if (!soe_id.equals(""))
						{
							StrSoe = " AND enquiry_soe_id IN (" + soe_id + ")";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							StrHTML = ListTAT();
							StrClosedHTML = SOESummary();
							StrClosedHTML1 = SOBSummary();
						}
					}
				} else {
					dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
					starttime = PadQuotes(request.getParameter("txt_starttime"));
					endtime = PadQuotes(request.getParameter("txt_endtime"));
					targetstarttime = starttime;
					targetendtime = endtime;
					chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
					StrHTML = ListTAT();
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
			// SOP("dr_branch_id=22==" + dr_branch_id);

		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		// SOP("branch_id====" + branch_id);
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
		// if (chk_team_lead.equals("on")) {
		// chk_team_lead = "1";
		// } else {
		// chk_team_lead = "0";
		// }

		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
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
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
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

	public String ListTAT() {
		int total = 0;
		int grand_total = 0;
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
		int total_avg = 0;
		String average = "";
		StringBuilder Str = new StringBuilder();
		try {
			String strqwert1 = " AND DATEDIFF(CONCAT(SUBSTR(so_date, 1, 8),SUBSTR(so_entry_date, 9, 6)),"
					+ " CONCAT(SUBSTR(enquiry_date, 1, 8),SUBSTR(enquiry_entry_date, 9, 6))) >= qwerty1 ";
			String strqwert2 = " AND DATEDIFF(CONCAT(SUBSTR(so_date, 1, 8),SUBSTR(so_entry_date, 9, 6)),"
					+ " CONCAT(SUBSTR(enquiry_date, 1, 8),SUBSTR(enquiry_entry_date, 9, 6))) <= qwerty2 ";
			String strsocount = " (SELECT COUNT(distinct so_id) FROM " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id ";
			if (!model_id.equals("")) {
				strsocount += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id AND soitem_rowcount != 0"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id";
			}
			strsocount += " WHERE so_emp_id = emp_id " + StrSoe + StrModel + StrBranch.replace("team_branch_id", "so_branch_id")
					+ " AND so_active = 1 AND so_date >= " + targetstarttime + " AND so_date <= " + targetendtime;

			StrSql = " SELECT emp_name, ";

			for (int i = 0; i <= 10; i++) {
				StrSql += strsocount + strqwert1.replace(">= qwerty1", " = " + i + ")") + " AS '" + i + "Days',";
			}
			for (int i = 11; i <= 30; i += 5) {
				StrSql += strsocount + strqwert1.replace("qwerty1", i + "") + strqwert2.replace("qwerty2", (i + 4) + "") + ") AS '" + i + "-" + (i + 4) + "Days',";
			}

			StrSql += strsocount + strqwert1.replace(">= qwerty1", " > 30)") + " AS '>30Days', ";
			StrSql += strsocount.replace("(SELECT COUNT(distinct so_id)", "COALESCE((SELECT FORMAT(DATEDIFF(CONCAT(SUBSTR(so_date, 1, 8), SUBSTR(so_entry_date, 9, 6)), "
					+ "CONCAT(SUBSTR(enquiry_date, 1, 8), SUBSTR(enquiry_entry_date, 9, 6))) / count(DISTINCT so_id), 2)") + "), 0) AS average ";
			StrSql += " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id  "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id"
					+ " WHERE emp_sales='1' " + StrSearch + StrBranch + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			SOP("StrSql===TAT===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
			if (crs.isBeforeFirst()) {
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Sales Consultant</th>\n");
				Str.append("<th data-toggle=\"true\">0 Days</th>\n");
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
				Str.append("<th data-hide=\"phone, tablet\">25-30 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">>30 Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Total</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Avg Days</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					total = crs.getInt("0Days") + crs.getInt("1Days") + crs.getInt("2Days")
							+ crs.getInt("3Days") + crs.getInt("4Days") + crs.getInt("5Days")
							+ crs.getInt("6Days") + crs.getInt("7Days") + crs.getInt("8Days")
							+ crs.getInt("9Days") + crs.getInt("10Days") + crs.getInt("11-15Days")
							+ crs.getInt("16-20Days") + crs.getInt("21-25Days") + crs.getInt("26-30Days")
							+ crs.getInt(">30Days");
					grand_total += total;
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
					total_gt30days += crs.getInt(">30Days");
					total_avg += crs.getDouble("average");
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("emp_name") + "</td>");
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
					Str.append("<td valign=top align=right>" + crs.getString(">30Days") + "</td>");
					Str.append("<td valign=top align=right>" + total + "</td>");
					Str.append("<td valign=top align=right>" + crs.getDouble("average") + "</td>");
					Str.append("</tr>\n");
					total = 0;
				}
				average = (total_avg / (double) count) + "";
				Str.append("<tr align=center>\n");
				Str.append("<td valign=top align=right colspan=2><b>Total</b></td>");
				Str.append("<td valign=top align=right><b>" + total_0days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_1days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_2days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_3days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_4days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_5days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_6days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_7days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_8days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_9days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_10days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_11_15days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_16_20days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_21_25days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_26_30days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_gt30days + "</b></td>");
				Str.append("<td valign=top align=right><b>" + grand_total + "</b></td>");
				Str.append("<td valign=top align=right><b>" + deci.format(Double.parseDouble(average)) + "</b></td>");
				Str.append("</tr>\n");
			} else {
				Str.append("<br><br><br><br><b><center><font color=red>No TAT Found!</font></center></b><br><br>\n");
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String SOESummary() {
		int total = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_name, count(soe_id) AS Total";
			String StrJoin = " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_soe_id = soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id";
			if (!model_id.equals("")) {
				StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id AND soitem_rowcount != 0"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id";
			}
			StrJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id  "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id"
					+ " WHERE 1=1 "
					+ " AND emp_sales='1' " + StrSearch + StrSoe + StrModel + StrBranch.replace("team_branch_id", "so_branch_id")
					+ " AND so_active = 1 "
					+ " AND so_date >= " + targetstarttime + " AND so_date <= " + targetendtime
					+ " GROUP BY soe_id"
					+ " ORDER BY Total DESC";

			StrSql = StrSql + StrJoin;
			// SOP("StrSql===>>>>>>>SOESummary===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				chart_data = "[";
				while (crs.next()) {
					chart_data = chart_data + "{'type': '" + crs.getString("soe_name") + "', 'total':" + crs.getString("Total") + "},";
					chart_data_total = chart_data_total + crs.getInt("Total");
				}
				chart_data = chart_data.substring(0, chart_data.length() - 1) + "]";
				crs.beforeFirst();
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>SOE</th>");
				Str.append("<th width=\"10%\">%</th>");
				Str.append("<th width=\"10%\">Total</th>");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					total = total + crs.getInt("total");
					Str.append("<tr>");
					Str.append("<td>").append(crs.getString("soe_name")).append("</td>");
					Str.append("<td align=right>").append(getPercentage(crs.getInt("total"), chart_data_total)).append("</td>");
					Str.append("<td align=right>").append(crs.getString("total")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right>&nbsp;</td>");
				Str.append("<td align=right><b>Total: </b></td>");
				Str.append("<td align=right><b>").append(total).append("</b></td>");
				Str.append("</tr>");

			} else {
				NoChart = "No SOE Summary Found!";
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String SOBSummary() {
		int total = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_name, count(sob_id) AS Total";
			String StrJoin = " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_sob_id = sob_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id";
			if (!model_id.equals("")) {
				StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id AND soitem_rowcount != 0"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id";
			}
			StrJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id  "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team on team_id=teamtrans_team_id"
					+ " WHERE 1=1 "
					+ " AND emp_sales='1' " + StrSearch + StrSoe + StrModel + StrBranch.replace("team_branch_id", "so_branch_id")
					+ " AND so_active = 1 "
					+ " AND so_date >= " + targetstarttime + " AND so_date <= " + targetendtime
					+ " GROUP BY sob_id"
					+ " ORDER BY Total DESC";

			StrSql = StrSql + StrJoin;
			// SOP("StrSql===SOBSummary===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				chart_data1 = "[";
				while (crs.next()) {
					chart_data1 = chart_data1 + "{'type': '" + crs.getString("sob_name") + "', 'total':" + crs.getString("Total") + "},";
					chart_data_total1 = chart_data_total1 + crs.getInt("Total");
				}
				chart_data1 = chart_data1.substring(0, chart_data1.length() - 1) + "]";

				crs.beforeFirst();
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>SOB</th>");
				Str.append("<th width=\"10%\">%</th>");
				Str.append("<th width=\"10%\">Total</th>");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					total = total + crs.getInt("total");
					Str.append("<tr>");
					Str.append("<td>").append(crs.getString("sob_name")).append("</td>");
					Str.append("<td align=right>").append(getPercentage(crs.getInt("total"), chart_data_total1)).append("</td>");
					Str.append("<td align=right>").append(crs.getString("total")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right>&nbsp;</td>");
				Str.append("<td align=right><b>Total: </b></td>");
				Str.append("<td align=right><b>").append(total).append("</b></td>");
				Str.append("</tr>");

			} else {
				NoChart1 = "No SOB Summary Found!";
			}
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSoe()
	{
		String sb = "";
		try
		{
			StrSql = " select soe_id, soe_name "
					+ " from " + compdb(comp_id) + "axela_soe "
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_inventory_item on item_model_id=model_id"
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

}
