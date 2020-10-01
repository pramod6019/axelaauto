package axela.sales;
// S Nag,, 20,21 may 2013
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_SOE_Monthly extends Connect {

	public String StrSql = "";
	public static String msg = "";
	public String emp_id = "0", branch_id = "";
	public String[] team_ids, exe_ids, soe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", soe_id = "", brand_id = "", region_id = "";
	public String StrHTML = "", StrHTML1 = "";
	public String BranchAccess = "", ExeAccess = "";
	public String StrSearch = "", SOESearch = "", regionSearch = "";
	public String go = "";
	String Strbrand = "";
	public String comp_id = "0";
	public String dr_branch_id = "0", chk_team_lead = "0";
	public String month = "", year = "", current_month = "";
	public int current_year = 0, month_nod = 0;
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			// SOP(Str);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				// SOP("afaff===" + BranchAccess);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				current_year = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				current_month = ToLongDate(kknow()).substring(4, 6);
				month = PadQuotes(request.getParameter("dr_month"));
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				if (month.equals("")) {
					month = current_month;
				}
				year = PadQuotes(request.getParameter("dr_year"));
				if (year.equals("")) {
					year = Integer.toString(current_year);
				}
				GetValues(request, response);
				if (go.equals("Go")) {

					StrSearch = ExeAccess;

					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("")) {
						StrSearch = StrSearch + " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch = StrSearch + " AND branch_region_id IN (" + region_id + ")";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ") "
								+ " AND team_branch_id IN (SELECT team_branch_id  FROM " + compdb(comp_id) + "axela_sales_team  WHERE team_id IN(" + team_id + "))";
					}
					if (!soe_id.equals("")) {
						SOESearch = SOESearch + " and enquiry_soe_id in (" + soe_id + ")";
					}
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListSOEMonthly();
						// StrHTML1 = ListSOE();
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
		month_nod = NoOfDaysInMonth(Integer.parseInt(month), Integer.parseInt(year));
		// SOP("month_nod---------" + month_nod);
		if (branch_id.equals("0")) {
			dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
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
		// SOP("branch id===----==" + branch_id);
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
		chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
		if (chk_team_lead.equals("on")) {
			chk_team_lead = "1";
		} else {
			chk_team_lead = "0";
		}
		// SOP("chk_team_lead == "+chk_team_lead);
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
	}

	public String ListSOEMonthly() {
		int total_monthly_Ecount = 0;
		int total_monthly_Scount = 0;
		StringBuilder Str = new StringBuilder();
		String enquiry_date = year + month;
		int team_monthly_Ecount = 0;
		int team_monthly_Scount = 0;
		int team_Eday_count[] = new int[month_nod];
		int team_Sday_count[] = new int[month_nod];
		int day_Ecount[] = new int[month_nod];
		int day_Scount[] = new int[month_nod];
		String teamName_team_id = "", teamtotal_team_id = "", str_team_day_Ecount = "", str_team_day_Scount = "";
		// String teamName_team_id = "", teamtotal_team_id = "", str_team_day_count = "";
		// SOP("enquiry_date===" + enquiry_date);
		StrSql = "SELECT emp_name, emp_id, emp_ref_no, "
				+ " COALESCE(weeklyoff_name,'') AS weeklyoff_name, "
				+ " team_id, team_name, team_branch_id,";
		for (int i = 1; i <= month_nod; i++) {

			StrSql = StrSql + "COUNT( DISTINCT CASE WHEN SUBSTR(enquiry_date, 1, 8) =" + enquiry_date + doublenum(i)
					+ " THEN enquiry_id END ) AS Eday" + i + ", "
					+ " COUNT( DISTINCT CASE WHEN SUBSTR(so_date, 1, 8) =" + enquiry_date + doublenum(i)
					+ " THEN so_id END ) AS Sday" + i;

			if (i != month_nod) {
				StrSql = StrSql + ",";
			}
		}
		StrSql = StrSql + " from " + " " + compdb(comp_id) + "axela_emp"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = team_branch_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_weeklyoff ON weeklyoff_id = emp_weeklyoff_id"
				+ " LEFT JOIN (SELECT enquiry_id, enquiry_emp_id, enquiry_date, enquiry_soe_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE SUBSTR(enquiry_date, 1, 6) = " + enquiry_date + ""
				+ BranchAccess.replace("branch_id", "enquiry_branch_id")
				+ ExeAccess.replace("emp_id", "enquiry_emp_id");
		if (!soe_id.equals("")) {
			StrSql += SOESearch;
		}
		if (!region_id.equals("")) {
			StrSql += regionSearch;
		}
		StrSql += "";
		StrSql += ") AS e ON e.enquiry_emp_id = emp_id"
				+ " LEFT JOIN (SELECT so_id, so_emp_id, so_date, enquiry_soe_id "
				+ " FROM  " + compdb(comp_id) + "axela_sales_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
				+ " WHERE SUBSTR(so_date, 1, 6) = " + enquiry_date + ""
				+ BranchAccess.replace("branch_id", "so_branch_id")
				+ ExeAccess.replace("emp_id", "so_emp_id");
		if (!soe_id.equals("")) {
			StrSql += SOESearch;
		}
		if (!region_id.equals("")) {
			StrSql += regionSearch;
		}
		StrSql += "";
		StrSql += " AND so_active = 1) AS s ON s.so_emp_id = emp_id"
				+ " WHERE 1=1 AND branch_active = 1"
				+ " AND emp_active = 1"
				+ StrSearch
				+ BranchAccess.replace("branch_id", "team_branch_id")
				+ ExeAccess
				+ " GROUP BY team_id, emp_id"
				+ " order by team_name, emp_name ";
		// SOP("soe query ====111=== " + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered table-hover\">\n");
				Str.append("<table class=\"table table-bordered \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th style=\"text-align:center\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Team</th>\n");
				Str.append("<th data-hide=\"phone\">Day Off</th>\n");
				for (int i = 1; i <= month_nod; i++) {
					Str.append("<th colspan=2 data-hide=\"phone\">").append(doublenum(i)).append("</th>\n");
				}
				Str.append("<th colspan=2 data-hide=\"phone\">Total</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0, team_count = 0;
				while (crs.next()) {
					count++;
					int monthly_Ecount = 0;
					int monthly_Scount = 0;
					if (teamtotal_team_id.equals("")) {
						teamtotal_team_id = crs.getString("team_id");
					}
					if (!teamtotal_team_id.equals(crs.getString("team_id"))) {
						Str.append("<tr><td>&nbsp</td><td valign=top nowrap align=right><b>").append("Team Total:").append("</b>");
						Str.append("</td><td>&nbsp</td>");
						for (int i = 1; i <= month_nod; i++) {
							if (team_Eday_count[i - 1] == 0) {
								str_team_day_Ecount = "";
								str_team_day_Scount = "";
							} else {
								str_team_day_Ecount = Integer.toString(team_Eday_count[i - 1]);
								str_team_day_Scount = Integer.toString(team_Sday_count[i - 1]);
							}
							if (str_team_day_Ecount.equals("0")) {
								str_team_day_Ecount = "";
							}
							if (str_team_day_Scount.equals("0")) {
								str_team_day_Scount = "";
							} else {
								str_team_day_Scount = "<font color='red'>" + str_team_day_Scount + "</font>";
							}
							Str.append("<td valign=top align=right><b>").append(str_team_day_Ecount).append("</b></td>");
							Str.append("<td valign=top align=right><b><font color='red'>").append(str_team_day_Scount).append("</font></b></td>");
						}

						Str.append("<td valign=top align=right><b>").append(team_monthly_Ecount).append("</b></td>");
						Str.append("<td valign=top align=right><b><font color='red'>").append(team_monthly_Scount).append("</font></b></td>");
						Str.append("</tr>");
						teamtotal_team_id = crs.getString("team_id");
						team_monthly_Ecount = 0;
						team_monthly_Scount = 0;
						team_Eday_count = new int[month_nod];
						team_Sday_count = new int[month_nod];
					}
					for (int i = 1; i <= month_nod; i++) {
						// SOP("aaaaaaa");
						// SOP("month_nod---2-------" + month_nod);
						// SOP("day" + i + ":" + CNumeric(crs.getString("day" + i)));
						team_Eday_count[i - 1] = team_Eday_count[i - 1] + Integer.parseInt(CNumeric(crs.getString("Eday" + i)));
						team_Sday_count[i - 1] = team_Sday_count[i - 1] + Integer.parseInt(CNumeric(crs.getString("Sday" + i)));
						// SOP("bbb");
						team_monthly_Ecount = team_monthly_Ecount + Integer.parseInt(CNumeric(crs.getString("Eday" + i)));
						team_monthly_Scount = team_monthly_Scount + Integer.parseInt(CNumeric(crs.getString("Sday" + i)));
						// SOP("team_day_count===" + team_day_count[i - 1]);
					}
					// SOP("333");
					if (!teamName_team_id.equals(crs.getString("team_id"))) {
						team_count++;
						Str.append("<tr>");
						if (chk_team_lead.equals("1")) {
							Str.append("<td valign=top align=center>").append(team_count).append("</td>");
						}
						if (chk_team_lead.equals("0")) {
							Str.append("<td>&nbsp</td>");
						}
						Str.append("<td colspan=34 valign=top align=left>").append("<b>");
						Str.append("<a href=\"../sales/team-list.jsp?team_id=").append(crs.getString("team_id"));
						Str.append("&dr_branch=").append(crs.getString("team_branch_id"));
						Str.append("\">").append(crs.getString("team_name")).append("</a></b></td></tr>");
						teamName_team_id = crs.getString("team_id");
					}
					if (chk_team_lead.equals("0")) {
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>");
						Str.append("<td valign=top align=left><a href=\"../portal/executive-summary.jsp?emp_id=");
						Str.append(crs.getString("emp_id")).append(" \">").append(crs.getString("emp_name"));
						Str.append(" (").append(crs.getString("emp_ref_no")).append(")</a></td>");
						Str.append("<td valign=top align=left>").append(crs.getString("weeklyoff_name")).append("</td>");
					}

					for (int i = 1; i <= month_nod; i++) {
						if (chk_team_lead.equals("0")) {
							String ECount = crs.getString("Eday" + i);
							String SCount = crs.getString("Sday" + i);
							if (ECount.equals("0")) {
								ECount = "";
							}
							if (SCount.equals("0")) {
								SCount = "";
							}
							Str.append("<td valign=top align=right>").append(ECount).append("</td>");
							Str.append("<td valign=top align=right><font color='red'>").append(SCount).append("</font></td>");
						}
						day_Ecount[i - 1] = day_Ecount[i - 1] + Integer.parseInt(CNumeric(crs.getString("Eday" + i)));
						day_Scount[i - 1] = day_Scount[i - 1] + Integer.parseInt(CNumeric(crs.getString("Sday" + i)));
						monthly_Ecount = monthly_Ecount + Integer.parseInt(CNumeric(crs.getString("Eday" + i)));
						monthly_Scount = monthly_Scount + Integer.parseInt(CNumeric(crs.getString("Sday" + i)));

					}
					if (chk_team_lead.equals("0")) {
						Str.append("<td valign=top align=right><b>").append(monthly_Ecount).append("</b></td>");
						Str.append("<td valign=top align=right><b><font color='red'>").append(monthly_Scount).append("</font></b></td>");
						Str.append("</tr>\n");
					}

					total_monthly_Ecount = total_monthly_Ecount + monthly_Ecount;
					total_monthly_Scount = total_monthly_Scount + monthly_Scount;
				}
				Str.append("<tr>");
				Str.append("<td>&nbsp</td>");
				Str.append("<td valign=top align=right><b>").append("Team Total:").append("</b></td>");
				Str.append("<td>&nbsp</td>");
				for (int i = 1; i <= month_nod; i++) {
					if (team_Eday_count[i - 1] == 0) {
						str_team_day_Ecount = "";
						str_team_day_Scount = "";
					} else {
						str_team_day_Ecount = Integer.toString(team_Eday_count[i - 1]);
						str_team_day_Scount = Integer.toString(team_Sday_count[i - 1]);
					}
					Str.append("<td valign=top align=right><b>").append(str_team_day_Ecount).append("</b></td>");
					Str.append("<td valign=top align=right><b><font color='red'>").append(str_team_day_Scount).append("</font></b></td>");
				}
				Str.append("<td valign=top align=right><b>").append(team_monthly_Ecount).append("</b></td>");
				Str.append("<td valign=top align=right><b><font color='red'>").append(team_monthly_Scount).append("</font></b></td>");
				Str.append("</tr>");
				Str.append("<tr >\n");
				Str.append("<td colspan=3 align=right><b>Total:</b></td>");
				for (int i = 0; i < month_nod; i++) {
					Str.append("<td colspan=1 align=right><b>").append(day_Ecount[i]);
					Str.append("<td colspan=1 align=right><b><font color='red'>").append(day_Scount[i]);
					Str.append("<font></b></td>");
				}
				Str.append("<td align=right><b>").append(total_monthly_Ecount).append("</b></td>");
				Str.append("<td align=right><b><font color='red'>").append(total_monthly_Scount).append("</font></b></td>");
				Str.append("</tr>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String ListSOE() {
		int total_monthly_count = 0;
		StringBuilder Str = new StringBuilder();
		String enquiry_date = year + month;
		int team_monthly_count = 0;
		int team_day_count[] = new int[month_nod];
		int day_count[] = new int[month_nod];
		String teamName_team_id = "", teamtotal_team_id = "", str_team_day_count = "";
		String soe_name = "";
		String StrSoe = "", soe_id = "0";

		try {
			StrSql = " SELECT soe_id, soe_name,"
					+ " COUNT(DISTINCT soe_id) AS 'soecount'"
					+ " FROM " + compdb(comp_id) + "axela_soe "
					+ " WHERE 1=1"
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			// SOP("rssoe ----------- " + StrSqlBreaker(StrSql));
			ResultSet rssoe = processQuery(StrSql, 0);

			while (rssoe.next()) {
				// if (!rssoe.getString("soe_name").equals(soe_name)) {
				// StrSoe +=
				// " (SELECT COUNT(DISTINCT enquiry_id) AS enquirycount FROM " +
				// compdb(comp_id) + "axela_sales_enquiry "
				// // + " INNER JOIN  " + compdb(comp_id) +
				// "axela_soe on soe_id=enquiry_soe_id"
				// + " WHERE 1 = 1 "
				// + " AND enquiry_soe_id = " + rssoe.getString("soe_id")
				// + " AND enquiry_emp_id = emp_id " + SoeSearch
				// + ") AS '" + rssoe.getString("soe_name") + " ',";
				// soe_id = rssoe.getString("soe_id");
				// SOP("soe_id--------" + soe_id);
				// }
				StrSoe += " SUM(IF(enquiry_soe_id=" + rssoe.getString("soe_id") + ",1,0)) AS '" + rssoe.getString("soe_name") + "',";
				// StrSoe = StrSoe.substring(0, StrSoe.length() - 1);

			}
			// while (rssoe.next()) {
			// StrSoe += "  COUNT(DISTINCT soe_id) AS 'soecount',";
			//
			// }
			StrSql = "SELECT emp_name, emp_id, emp_ref_no, team_id, team_name, "
					+ StrSoe
					+ " team_branch_id "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_emp_id=emp_id  "
					// + " INNER JOIN  " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id  "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id=teamtrans_team_id"
					+ " WHERE 1 = 1 " + StrSearch + ""
					// + " AND enquiry_emp_id = emp_id "
					+ " GROUP BY team_id, emp_id"
					+ " ORDER BY team_name, emp_name ";
			// SOP("SOE query------------" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				Str.append("<tr>");
				Str.append("<th data-hide=\"phone\">#</th>");
				Str.append("<th data-toggle=\"true\">Team</th>\n");
				rssoe.beforeFirst();
				while (rssoe.next()) {
					Str.append("<th data-toggle=\"true\">" + rssoe.getString("soe_name") + "</th>\n");
				}
				Str.append("<th data-toggle=\"true\">Total</th>\n");
				Str.append("</tr>\n");

				int count = 0, team_count = 0;
				HashMap<String, Integer> enqtotal = new HashMap<>();
				rssoe.beforeFirst();
				while (rssoe.next()) {
					// SOP("soecount----1----" + rssoe.getInt("soecount"));
					for (int i = 1; i <= rssoe.getInt("soecount"); i++) {
						enqtotal.put(rssoe.getString("soe_name"), 0);
					}
				}
				crs.last();

				int soetotal = 0;
				int rowsoetotal = 0;
				int grandsoetotal = 0;
				crs.beforeFirst();
				while (crs.next()) {
					// SOP("chk_team_lead-----" + chk_team_lead);
					count++;
					int monthly_count = 0;
					if (teamtotal_team_id.equals("")) {
						teamtotal_team_id = crs.getString("team_id");
					}

					if (!teamtotal_team_id.equals(crs.getString("team_id"))) {
						Str.append("<tr><td>&nbsp</td>");
						Str.append("<td valign=top nowrap align=right><b>").append("Team Total:").append("</b>");
						Str.append("</td>");
						rssoe.beforeFirst();
						while (rssoe.next()) {
							for (int i = 1; i <= rssoe.getInt("soecount"); i++) {
								soetotal = crs.getInt(rssoe.getString("soe_name"));
								// Str.append("<td valign=top align=right>");
								if ((crs.getInt(rssoe.getString("soe_name")) == 0))
								{
									soetotal = crs.getInt(rssoe.getString("soe_name"));
								}
								else {
									soetotal = crs.getInt(rssoe.getString("soe_name"));
								}
								// Str.append("</td>");
								Str.append("<td valign=top align=right>" + soetotal + "</td>");
								// Str.append("<td align=right>" + soetotal +
								// "</td>\n");
							}
						}

						Str.append("<td valign=top align=right><b>").append(team_monthly_count).append("</b></td>");
						Str.append("</tr>");
						teamtotal_team_id = crs.getString("team_id");
						team_monthly_count = 0;
						team_day_count = new int[rssoe.getInt("soecount")];

					}

					if (!teamName_team_id.equals(crs.getString("team_id"))) {

						// SOP("inside---------team");
						team_count++;
						Str.append("<tr>");
						if (chk_team_lead.equals("1")) {
							Str.append("<td valign=top align=center>").append(team_count).append("</td>");
						}
						if (chk_team_lead.equals("0")) {
							Str.append("<td>&nbsp</td>");
						}
						Str.append("<td colspan=9 valign=top align=left>").append("<b>");
						Str.append("<a href=\"../sales/team-list.jsp?team_id=").append(crs.getString("team_id"));
						Str.append("&dr_branch=").append(crs.getString("team_branch_id"));
						Str.append("\">").append(crs.getString("team_name")).append("</a></b></td></tr>");
						teamName_team_id = crs.getString("team_id");
					}
					if (chk_team_lead.equals("0")) {
						// SOP("inside -------chk_team_lead");
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>");
						Str.append("<td valign=top align=left><a href=\"../portal/executive-summary.jsp?emp_id=");
						Str.append(crs.getString("emp_id")).append(" \">").append(crs.getString("emp_name"));
						Str.append(" (").append(crs.getString("emp_ref_no")).append(")</a></td>");
						// Str.append("<td valign=top align=left>").append(crs.getString("weeklyoff_name")).append("</td>");
					}
					rssoe.beforeFirst();
					while (rssoe.next()) {
						for (int i = 0; i < rssoe.getInt("soecount"); i++) {
							soetotal += crs.getInt(rssoe.getString("soe_name"));
							Str.append("<td align=right>");
							if ((crs.getInt(rssoe.getString("soe_name")) == 0))
							{
								Str.append("&nbsp");
							}
							else {
								Str.append("" + crs.getInt(rssoe.getString("soe_name")) + "");
							}
							Str.append("</td>");
							enqtotal.put(rssoe.getString("soe_name") + "", enqtotal.get(rssoe.getString("soe_name") + "") + crs.getInt(rssoe.getString("soe_name")));
						}
					}
					if (chk_team_lead.equals("0")) {
						Str.append("<td valign=top align=right><b>").append(monthly_count).append("</b></td>");
						Str.append("</tr>\n");
					}

					total_monthly_count = total_monthly_count + monthly_count;
				}
				rowsoetotal += soetotal;
				Str.append("<tr>");
				Str.append("<td>&nbsp</td>");
				Str.append("<td valign=top align=right><b>").append("Team Total:").append("</b></td>");
				Str.append("<td colspan=9 valign=top align=right><b>").append(soetotal).append("</b></td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td colspan=2 align=right><b>Total:</b></td>");
				rssoe.beforeFirst();
				while (rssoe.next()) {
					for (int i = 1; i <= rssoe.getInt("soecount"); i++) {
						Str.append("<td align=right><b>" + soetotal + "</b></td>\n");
					}
				}
				Str.append("<td valign=top align=right><b>").append(rowsoetotal).append("</b></td>");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id = " + dr_branch_id + " "
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1' AND emp_sales = '1' AND "
					+ " (emp_branch_id = " + dr_branch_id + " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";
			if (!team_id.equals("")) {
				StrSql = StrSql + " AND teamtrans_team_id in (" + team_id + ")";
			}
			StrSql = StrSql
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" ");
			Str.append("size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateMonth() {
		if (month.equals("")) {
			month = current_month;
		}
		StringBuilder Str = new StringBuilder();
		for (int i = 1, j = 0; i <= 12; i++, j++) {
			Str.append("<option value = ").append(doublenum(i)).append("");
			Str.append(StrSelectdrop(doublenum(i), month)).append(">");
			Str.append(TextMonth(j)).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateYear() {

		StringBuilder Str = new StringBuilder();
		for (int i = current_year - 3; i <= current_year + 3; i++) {
			Str.append("<option value=").append(i).append("");
			Str.append(StrSelectdrop(Integer.toString(i), year));
			Str.append(">").append(i).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name  "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_soe_id = soe_id "
					+ " WHERE 1 = 1 "
					+ " GROUP BY soe_id "
					+ " ORDER BY soe_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("soe_id"), soe_ids));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public int NoOfDaysInMonth(int month, int year) {
		int days = 0;
		// int month = Integer.parseInt(this.month);
		// int year = Integer.parseInt(this.year);
		boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
		switch (month) {
			case 1 :
				days = 31;
				break;
			case 2 :
				if (isLeapYear) {
					days = 29;
				} else {
					days = 28;
				}
				break;
			case 3 :
				days = 31;
				break;
			case 4 :
				days = 30;
				break;
			case 5 :
				days = 31;
				break;
			case 6 :
				days = 30;
				break;
			case 7 :
				days = 31;
				break;
			case 8 :
				days = 31;
				break;
			case 9 :
				days = 30;
				break;
			case 10 :
				days = 31;
				break;
			case 11 :
				days = 30;
				break;
			case 12 :
				days = 31;
				break;
		}
		return days;
	}
}
